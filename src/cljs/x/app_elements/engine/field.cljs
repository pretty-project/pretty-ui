
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v1.1.6
; Compatibility: x4.3.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.field
    (:require [app-fruits.dom                   :as dom]
              [mid-fruits.candy                 :refer [param return]]
              [mid-fruits.css                   :as css]
              [mid-fruits.keyword               :as keyword]
              [mid-fruits.map                   :refer [dissoc-in]]
              [mid-fruits.string                :as string]
              [mid-fruits.vector                :as vector]
              [x.app-core.api                   :as a :refer [r]]
              [x.app-elements.engine.element    :as element]
              [x.app-elements.engine.input      :as input]
              [x.app-elements.engine.surface    :as surface]
              [x.app-elements.engine.targetable :as targetable]
              [x.app-environment.api            :as environment]
              [x.app-locales.api                :as locales]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (px)
(def FIELD-LINE-HEIGHT 24)

; @constant (px)
(def ADORNMENT-WIDTH 24)

; @constant (px)
(def DEFAULT-FIELD-PADDING 12)

; @constant (px)
(def FIELD-BORDER-WIDTH 1)

; @constant (ms)
;  Az utolsó karakter leütése után mennyi idő elteltével számít
;  befejezettnek a gépelés
(def TYPE-ENDED-AFTER 250)

; @constant (px)
(def CARET-OFFSET 4)



;; -- Presets -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-field-adornment-preset
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  ;  {:icon (keyword)
  ;   :on-click (vector)
  ;   :preset (keyword)}
  [field-id]
  {:icon :clear :on-click [:x.app-elements/empty-field! field-id]
   ; XXX#8073
   ; Az adornment a {:preset ...} tulajdonság értékével azonosítható
   :preset :empty-field-adornment
   :tooltip :empty-field!})

(defn reset-field-adornment-preset
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  ;  {:icon (keyword)
  ;   :on-click (vector)
  ;   :preset (keyword)}
  [field-id]
  {:icon :undo :on-click [:x.app-elements/reset-input! field-id]
   ; XXX#8073
   :preset :reset-field-adornment
   :tooltip :reset-field!})



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props->on-change-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Az {:on-type-ended ...} tulajdonságkét átadott esemény kizárólag
  ; event-vector formátumban használható, ugyanis az (a/dispatch-last ...)
  ; függvény nem kezel metamorphic-event formátumú eseményeket.
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:modifier (function)(opt)
  ;   :on-change (metamorphic-event)(opt)
  ;   :on-type-ended (event-vector)(opt)
  ;   :value-path (item-path vector)}
  ;
  ; @return (function)
  [field-id {:keys [modifier on-change on-type-ended surface value-path]}]
  #(let [value (dom/event->value %)
         value (if (some?    modifier)
                   (modifier value)
                   (param    value))]
        (if (input/value-path->vector-item? value-path)
            (a/dispatch-sync [:x.app-db/set-vector-item! value-path value])
            (a/dispatch-sync [:x.app-db/set-item!        value-path value]))
        (if (some? on-type-ended)
            (let [on-type-ended (a/event-vector<-params on-type-ended value)]
                 (a/dispatch-last TYPE-ENDED-AFTER on-type-ended)))
        (if (some? on-change)
            (let [on-change (a/metamorphic-event<-params on-change value)]
                 (a/dispatch on-change)))
        (if (some? surface)
            (a/dispatch [:x.app-elements/show-surface! field-id]))))

(defn field-props->start-adornments-padding
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;  {:start-adornments (maps in vector)(opt)}
  ;
  ; @return (integer)
  [{:keys [start-adornments]}]
  (if (vector/nonempty? start-adornments)
      (* (count start-adornments) ADORNMENT-WIDTH)
      (return DEFAULT-FIELD-PADDING)))

(defn field-props->end-adornments-padding
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;  {:end-adornments (maps in vector)(opt)}
  ;
  ; @return (integer)
  [{:keys [end-adornments]}]
  (if (vector/nonempty? end-adornments)
      (* (count end-adornments) ADORNMENT-WIDTH)
      (return DEFAULT-FIELD-PADDING)))

(defn view-props->field-filled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:value (string)}
  ;
  ; @return (boolean)
  [{:keys [value]}]
  (string/nonempty? value))

(defn view-props->lines-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:multiline? (boolean)(opt)
  ;   :value (string)}
  ;
  ; @return (integer)
  [{:keys [multiline? value]}]
  (if (boolean multiline?)
      (let [lines-count (inc (string/count-newlines value))]
           ; BUG#1481
           ; A textarea element magassága minimum 2 sor magasságú kell legyen,
           ; különben az egy sorba írt - a textarea szélességébe ki nem férő -
           ; szöveg nem törik meg automatikusan
           ; Google Chrome Version 89.0.4389.114
           (inc lines-count))
      (return 1)))

(defn view-props->field-height
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;
  ; @return (integer)
  [view-props]
  (+ (* FIELD-LINE-HEIGHT (view-props->lines-count view-props))))
    ; WARNING! DEPRECATED!
    ; Ettől 50px magas lett a search-field!
    ; (* FIELD-BORDER-WIDTH 2)
    ; WARNING! DEPRECATED!

(defn view-props->field-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;
  ; @return (map)
  ;  {:height (string)
  ;   :paddingLeft (string)
  ;   :paddingRight (string)}
  [view-props]
  {:height       (css/px (view-props->field-height              view-props))
   :paddingLeft  (css/px (field-props->start-adornments-padding view-props))
   :paddingRight (css/px (field-props->end-adornments-padding   view-props))})

(defn view-props->placeholder-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;
  ; @return (map)
  ;  {:left (string)}
  [view-props]
  {:left (css/px (+ (field-props->start-adornments-padding view-props)
                    (param CARET-OFFSET)))})

(defn view-props->render-field-placeholder?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:field-empty? (boolean)
  ;   :placeholder (metamorphic-content)}
  ;
  ; @return (boolean)
  [{:keys [field-empty? placeholder]}]
  (and (some?   placeholder)
       (boolean field-empty?)))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-blur-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (function)
  [field-id]
  #(a/dispatch [:x.app-elements/->field-blurred field-id]))

(defn on-focus-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (function)
  [field-id]
  #(a/dispatch [:x.app-elements/->field-focused field-id]))

(defn field-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;  {:auto-focus? (boolean)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :max-length (integer)(opt)
  ;   :targetable? (boolean)(opt)
  ;   :type (keyword)(opt)
  ;    :password, :text
  ;   :value (string)}
  ;
  ; @return (map)
  ;  {:autoFocus (boolean)
  ;   :disabled (boolean)
  ;   :id (string)
  ;   :max-length (integer)
  ;   :on-blur (function)
  ;   :on-focus (function)
  ;   :on-change (function)
  ;   :style (map)
  ;   :value (string)}
  [field-id {:keys [auto-focus? disabled? max-length surface targetable? type value]
             :as view-props}]
  (cond-> (param {})
          (boolean disabled?)
          (merge {:disabled   true
                  :style      (view-props->field-style view-props)
                  :type       (keyword/to-dom-value type)
                  :value      value})
          (not disabled?)
          (merge {:autoFocus  auto-focus?
                  :max-length max-length
                  :on-blur    (on-blur-function        field-id)
                  :on-focus   (on-focus-function       field-id)
                  :style      (view-props->field-style view-props)
                  :type       (keyword/to-dom-value    type)
                  :value      value

                  ; BUG#8041
                  ;  Abban az esetben, ha egy input elem {:value-path [...]}
                  ;  tulajdonságaként átadott Re-Frame adatbázis útvonalon tárolt
                  ;  érték megváltozik egy külső esemény hatására, az input elem
                  ;  {:on-change #(...)} függvényétől függetlenül, miközben
                  ;  az input elemen van a fókusz, akkor az elem fókuszának
                  ;  elvesztésekor megvizsgálja és "észreveszi", hogy megváltozott
                  ;  az értéke, ezért lefuttatja az {:on-change #(...)} függvényét,
                  ;  aminek hatására nem várt események történhetnek, amik hibás
                  ;  működéshez vezethetnek.
                  ;  Pl.: a combo-box elem opciós listájából kiválasztott opció,
                  ;  ami az elem {:value-path [...]} ... útvonalon tárolódik,
                  ;  felülíródik az input tartalmával, ami minden esetben string
                  ;  típus, ellentétben a kiválaszott opcióval.
                  ;  Ezt elkerülendő, az elem a változásait az {:on-input #(...)}
                  ;  függvény használatával kezeli.
                  :on-input   (field-props->on-change-function field-id view-props)
                  ; BUG#8041
                  ;  A React hibás input elemként értelmezi, az {:on-change #(...)}
                  ;  függvény nélküli input elemeket.
                  :on-change #(let [])})

          (and (not     disabled?)
               (boolean targetable?))
          (merge {:id (targetable/element-id->target-id field-id)})
          (some? surface)
          (merge {:on-mouse-down #(a/dispatch [:x.app-elements/show-surface! field-id])})))

(defn field-placeholder-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;
  ; @return (map)
  ;  {:style (map)}
  [_ view-props]
  {:style (view-props->placeholder-style view-props)})



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-field-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (string)
  [db [_ field-id]]
  (let [input-value (r input/get-input-value db field-id)]
       (str input-value)))

(defn field-filled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [db [_ field-id]]
  (let [field-value (r get-field-value db field-id)]
       (string/nonempty? field-value)))

(defn field-empty?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [db [_ field-id]]
  (let [field-filled? (r field-filled? db field-id)]
       (not field-filled?)))

(defn field-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [db [_ field-id]]
  (r input/input-value-changed? db field-id))

(defn get-field-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :field-empty? (boolean)
  ;   :invalid-message (metamorphic-content)
  ;   :value (string)}
  [db [_ field-id]]
  (merge {:field-empty?   (r field-empty?    db field-id)
          :field-changed? (r field-changed?  db field-id)
          :value          (r get-field-value db field-id)}
         ; 1.
         (if (r input/input-required-success? db field-id)
             {:color :success})
         ; 2.
         (if (r input/input-value-invalid-warning? db field-id)
             {:color  :warning
              :invalid-message (r input/get-input-invalid-message db field-id)})
         ; 3.
         (if (r input/input-required-warning? db field-id)
             {:color  :warning
              :invalid-message :please-fill-out-this-field})))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mark-field-as-blurred!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (r element/set-element-prop! db field-id :focused? false))

(a/reg-event-db :x.app-elements/mark-field-as-blurred! mark-field-as-blurred!)

(defn mark-field-as-focused!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (r element/set-element-prop! db field-id :focused? true))

(a/reg-event-db :x.app-elements/mark-field-as-focused! mark-field-as-focused!)

(defn empty-field-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (let [value-path (r element/get-element-prop db field-id :value-path)]
       (assoc-in db value-path string/empty-string)))

(defn set-field-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (*) value
  ;
  ; @return (map)
  [db [_ field-id value]]
  (let [field-value (str value)]
       (r input/set-input-value! db field-id field-value)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-elements/reg-field-keypress-events?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [{:keys [db]} [_ field-id]]
      (let [emptiable? (r element/get-element-prop db field-id :emptiable?)
            on-enter   (r element/get-element-prop db field-id :on-enter)]
           {:dispatch-cond
            [(boolean emptiable?)
             [:x.app-environment.keypress-handler/reg-keypress-event!
              ::on-escape-pressed
              {:key-code  27
               :on-keyup  [:x.app-elements/empty-field! field-id]
               :required? true}]
             (some? on-enter)
             [:x.app-environment.keypress-handler/reg-keypress-event!
              ::on-enter-pressed
              {:key-code  13
               :on-keyup  on-enter
               :required? true}]]})))

(a/reg-event-fx
  :x.app-elements/remove-field-keypress-events?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [{:keys [db]} [_ field-id]]
      (let [emptiable? (r element/get-element-prop db field-id :emptiable?)
            on-enter   (r element/get-element-prop db field-id :on-enter)]
           {:dispatch-cond
            [(boolean emptiable?)
             [:x.app-environment.keypress-handler/remove-keypress-event!
              ::on-escape-pressed]
             (some? on-enter)
             [:x.app-environment.keypress-handler/remove-keypress-event!
              ::on-enter-pressed]]})))

(a/reg-event-fx
  :x.app-elements/empty-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [{:keys [db]} [_ field-id]]
      (let [on-empty-event (r element/get-element-prop db field-id :on-empty)
            field-value    (r get-field-value          db field-id)]
           {:db       (r empty-field-value! db field-id)
            :dispatch (a/metamorphic-event<-params on-empty-event field-value)})))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-elements/->field-blurred
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [{:keys [db]} [event-id field-id]]
      (let [on-blur-event (r element/get-element-prop db field-id :on-blur)]
           {:db (-> db (environment/enable-non-required-keypress-events! [event-id])
                       (mark-field-as-blurred!                           [event-id field-id])
                       (input/mark-input-as-visited!                     [event-id field-id])
                       (surface/hide-surface!                            [event-id field-id]))
            :dispatch-n [[:x.app-elements/remove-field-keypress-events?! field-id]
                         (param on-blur-event)]})))

                         ; WARNING#9055
                         ; x4.3.9
                         ; Az :x.app-elements/resolve-change-listener?! eseményt nem sikerült
                         ; DB függvényként értelmezve meghívni, mert az esemény olyan adatbázis
                         ; műveleteket tartalmaz, amik nem léteznek az x.app-db.api modulban.
                         ;
                         ; [:x.app-elements/resolve-change-listener?! field-id]

(a/reg-event-fx
  :x.app-elements/->field-focused
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [{:keys [db]} [event-id field-id]]
      (let [on-focus-event (r element/get-element-prop db field-id :on-focus)]
           {:db (-> db (environment/disable-non-required-keypress-events! [event-id])
                       (mark-field-as-focused!                            [event-id field-id]))
            :dispatch-n [[:x.app-elements/reg-field-keypress-events?! field-id]
                         (param on-focus-event)]})))

                         ; WARNING#9055
                         ; [:x.app-elements/reg-change-listener?! field-id]
