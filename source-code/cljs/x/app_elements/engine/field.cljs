
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v1.3.0
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.field
    (:require [app-fruits.dom                        :as dom]
              [mid-fruits.candy                      :refer [param return]]
              [mid-fruits.css                        :as css]
              [mid-fruits.string                     :as string]
              [mid-fruits.vector                     :as vector]
              [x.app-core.api                        :as a :refer [r]]
              [x.app-elements.surface-handler.events :as surface-handler.events]
              [x.app-elements.engine.element         :as element]
              [x.app-elements.engine.input           :as input]
              [x.app-elements.target-handler.helpers :as target-handler.helpers]
              [x.app-environment.api                 :as environment]

              [x.app-db.api :as db]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (px)
(def FIELD-LINE-HEIGHT 24)

; @constant (px)
(def FIELD-VERTICAL-PADDING 3)

; @constant (px)
(def FIELD-BORDER-WIDTH 1)

; @constant (ms)
;  Az utolsó karakter leütése után mennyi idő elteltével számít befejezettnek a gépelés
(def TYPE-ENDED-AFTER 350)
;(def TYPE-ENDED-AFTER 250) <- Nem mindenki programozó! :)



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
  {:icon :clear :on-click [:elements/empty-field! field-id]
   ; XXX#8073
   ; Az adornment a {:preset ...} tulajdonság értékével azonosítható
   :preset :empty-field-adornment
   ; A mező kiürítése az ESC billentyű lenyomásával is vezérelhető, ezért nem szükséges indexelni.
   ; XXX#6054
   ; Az indexelt adornment gombok a TAB billentyűvel való mezők közötti váltást nehezítik!
   ; A billentyűvel is vezérelt adornment gombokon ezért célszerű az indexelést kikapcsolni.
   :tab-indexed? false
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
  {:icon :undo :on-click [:elements/reset-input! field-id]
   ; XXX#8073
   :preset  :reset-field-adornment
   :tooltip :reset-field!})



;; -- Helpers -----------------------------------------------------------------
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
  ;   :on-change (metamorphic-event)(opt)
  ;   :on-type-ended (event-vector)(opt)
  ;   :value-path (vector)}
  ;
  ; @return (function)
  [field-id {:keys [modifier on-change on-type-ended surface value-path]}]
  #(let [value (dom/event->value %)
         value (if modifier (modifier value)
                            (param    value))]
        ; *
        (if (input/value-path->vector-item? value-path)
            (a/dispatch-sync [:db/set-vector-item! value-path value])
            (a/dispatch-sync [:db/set-item!        value-path value]))
        ; Dispatch on-type-ended event if ...
        (if on-type-ended (let [on-type-ended (a/event-vector<-params on-type-ended value)]
                               (a/dispatch-last TYPE-ENDED-AFTER on-type-ended)))
        ; Dispatch on-change event if ...
        (if on-change (let [on-change (a/metamorphic-event<-params on-change value)]
                           (a/dispatch on-change)))
        ; Reveal surface if ...
        (if surface (a/dispatch [:elements/show-surface! field-id]))))




(defn store-field-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (string) value
  ;
  ; @return (map)
  [db [_ field-id value]]
  (let [value-path (r element/get-element-prop db field-id :value-path)]
       (if (input/value-path->vector-item? value-path)
           (r db/set-vector-item! value-path value)
           (r db/set-item!        value-path value))))

(defn update-field-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (string) value
  ;
  ; @return (map)
  [db [_ field-id value]]
  (if-let [modifier (r element/get-element-prop db field-id :modifier)]
          (r store-field-value! db field-id (modifier value))
          (r store-field-value! db field-id (param    value))))

(a/reg-event-db :elements/update-field-value! update-field-value!)





(a/reg-event-fx
  :elements/field-changed
  (fn [{:keys []}]))

(defn field-props->field-filled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;  {:value (string)}
  ;
  ; @return (boolean)
  [{:keys [value]}]
  (string/nonempty? value))

(defn field-props->line-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;  {:multiline? (boolean)(opt)
  ;   :value (string)}
  ;
  ; @return (integer)
  [{:keys [multiline? value]}]
  (if multiline? ; If field is multiline ...
                 (let [line-count (-> value string/count-newlines inc)]
                      ; BUG#1481
                      ; A textarea element magassága minimum 2 sor magasságú kell legyen,
                      ; különben az egy sorba írt - a textarea szélességébe ki nem férő -
                      ; szöveg nem törik meg automatikusan
                      ; Google Chrome Version 89.0.4389.114
                      (inc line-count))
                 ; If field is NOT multiline ...
                 (return 1)))

(defn field-props->field-height
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;
  ; @return (integer)
  [field-props]
  (+ (* FIELD-LINE-HEIGHT (field-props->line-count field-props))
     (* FIELD-VERTICAL-PADDING 2)
     (* FIELD-BORDER-WIDTH     2)))

(defn field-props->field-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:height (string)}
  [field-props]
  {:height (-> field-props field-props->field-height css/px)})

(defn field-props->render-field-placeholder?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;  {:field-empty? (boolean)
  ;   :focused? (boolean)
  ;   :placeholder (metamorphic-content)}
  ;
  ; @return (boolean)
  [{:keys [field-empty? focused? placeholder]}]
  (and placeholder field-empty? (not focused?)))

(defn on-blur-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (function)
  [field-id]
  #(a/dispatch [:elements/field-blurred field-id]))

(defn on-focus-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (function)
  [field-id]
  #(a/dispatch [:elements/field-focused field-id]))

(defn field-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:auto-focus? (boolean)(opt)
  ;   :disable-autofill? (boolean)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :max-length (integer)(opt)
  ;   :name (keyword)
  ;   :type (keyword)(opt)
  ;    :password, :text
  ;   :value (string)}
  ;
  ; @return (map)
  ;  {:auto-complete (keyword)
  ;   :auto-focus (boolean)
  ;   :disabled (boolean)
  ;   :id (string)
  ;   :max-length (integer)
  ;   :name (keyword)
  ;   :on-blur (function)
  ;   :on-focus (function)
  ;   :on-change (function)
  ;   :style (map)
  ;   :value (string)}
  [field-id {:keys [auto-focus? disable-autofill? disabled? max-length name surface type value] :as field-props}]
          ; Az x4.4.9 verzióig az elemek target-id azonosítása a {:targetable? ...}
          ; tulajdonságuk értékétől függött. Az x4.4.9 verzió óta a target-id azonosítás
          ; minden esetben elérhető.
          ; A field típusú elemek target-id azonosítása nem kizárólag a {:targetable? ...}
          ; tulajdonságuk függvénye volt. A {:disabled? true} állapotban levő field elemek
          ; nem voltak azonosíthatók target-id használatával. Az x4.4.9 verzióban ez a feltétel
          ; (indoklás és ismert felhasználás hiányában) eltávolításra került.
  (cond-> {:id (target-handler.helpers/element-id->target-id field-id)}
          ; If field is disabled ...
          (boolean disabled?) (merge {:disabled true
                                      :type     type
                                      :value    value
                                      :style    (field-props->field-style field-props)
                                      ; BUG#8809
                                      ;  Ha a mező disabled állapotba lépéskor elveszítené az on-change tulajdonságát,
                                      ;  akkor a React figyelmeztetne, hogy controlled elemből uncontrolled elemmé változott!
                                      :on-change #(let [])})
          ; If field is NOT disabled ...
          (not disabled?) (merge {:auto-complete name
                                  :auto-focus    auto-focus?
                                  :max-length    max-length
                                  :name          name
                                  :type          type
                                  :value         value
                                  :on-blur  (on-blur-function         field-id)
                                  :on-focus (on-focus-function        field-id)
                                  :style    (field-props->field-style field-props)
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
                                  :on-input (field-props->on-change-function field-id field-props)
                                  ; BUG#8041
                                  ;  A React hibás input elemként értelmezi, az {:on-change #(...)}
                                  ;  függvény nélküli input elemeket.
                                  :on-change #(let [])})
          ; If field has surface ...
          (some? surface) (merge {:on-mouse-down #(a/dispatch [:elements/show-surface! field-id])})))



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

(defn get-field-props
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
         (if (r input/input-value-invalid-warning? db field-id)
             {:border-color :warning :invalid-message (r input/get-input-invalid-message db field-id)})
         ; 2.
         (if (r input/input-required-warning? db field-id)
             {:border-color :warning :invalid-message :please-fill-out-this-field})))



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

(a/reg-event-db :elements/mark-field-as-blurred! mark-field-as-blurred!)

(defn mark-field-as-focused!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (r element/set-element-prop! db field-id :focused? true))

(a/reg-event-db :elements/mark-field-as-focused! mark-field-as-focused!)

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

(defn field-blurred
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (as-> db % (r environment/quit-type-mode!          %)
             (r mark-field-as-blurred!               % field-id)
             (r input/mark-input-as-visited!         % field-id)
             (r surface-handler.events/hide-surface! % field-id)))

(defn field-focused
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (as-> db % (r environment/set-type-mode! %)
             (r mark-field-as-focused!     % field-id)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements/init-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [{:keys [db]} [_ field-id]]
      (if-let [auto-focus? (r element/get-element-prop db field-id :auto-focus?)]
              {:dispatch-n (let [on-focus-event (r element/get-element-prop db field-id :on-focus)]
                                [on-focus-event [:elements/reg-field-keypress-events! field-id]])
               :db (as-> db % (r input/init-input! % field-id)
                              (r field-focused     % field-id))}
              {:db (r input/init-input! db field-id)})))

(a/reg-event-fx
  :elements/reg-field-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [{:keys [db]} [_ field-id]]
      (let [emptiable? (r element/get-element-prop db field-id :emptiable?)
            on-enter   (r element/get-element-prop db field-id :on-enter)
            on-enter-props  {:key-code 13 :required? true :on-keydown on-enter}
            on-escape-props {:key-code 27 :required? true :on-keydown [:elements/empty-field! field-id]}]
           {:dispatch-cond [emptiable? [:environment/reg-keypress-event! ::on-escape-pressed on-escape-props]
                            on-enter   [:environment/reg-keypress-event! ::on-enter-pressed  on-enter-props]]})))

(a/reg-event-fx
  :elements/remove-field-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [{:keys [db]} [_ field-id]]
      (let [emptiable? (r element/get-element-prop db field-id :emptiable?)
            on-enter   (r element/get-element-prop db field-id :on-enter)]
           {:dispatch-cond [emptiable? [:environment/remove-keypress-event! ::on-escape-pressed]
                            on-enter   [:environment/remove-keypress-event! ::on-enter-pressed]]})))

(a/reg-event-fx
  :elements/empty-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [{:keys [db]} [_ field-id]]
      (if-let [input-enabled? (target-handler.helpers/element-id->target-enabled? field-id)]
              ; Az [:elements/empty-field! ...] esemény kizárólag abban az esetben törli a mező
              ; tartalmát, ha az input elem nincs disabled="true" állapotban, így elkerülhető
              ; a következő hiba:
              ; Pl.: A mező on-type-ended eseménye által indított request disabled="true" állapotba
              ;      állítja a mezőt, és a szerver válaszának megérkezéséig disabled="true" állapotban
              ;      levő (de fókuszált) mezőt lehetséges kiüríteni az ESC billentyő megnyomásával,
              ;      ami ismételten elindítaná a request-et (az on-empty esemény által)!
              (if-let [field-filled? (r field-filled? db field-id)]
                      ; Ha a mező üres, akkor az [:elements/empty-field! ...] hatás nélkül történik meg
                      (let [on-empty    (r element/get-element-prop db field-id :on-empty)
                            field-value (r get-field-value          db field-id)]
                           {:db         (r empty-field-value!       db field-id)
                            :dispatch   (a/metamorphic-event<-params on-empty field-value)})))))

(a/reg-event-fx
  :elements/field-blurred
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [{:keys [db]} [_ field-id]]
      (let [on-blur-event (r element/get-element-prop db field-id :on-blur)]
           {:db (r field-blurred db field-id)
            :dispatch-n [on-blur-event [:elements/remove-field-keypress-events! field-id]]})))

(a/reg-event-fx
  :elements/field-focused
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [{:keys [db]} [_ field-id]]
      (let [on-focus-event (r element/get-element-prop db field-id :on-focus)]
           {:db (r field-focused db field-id)
            :dispatch-n [on-focus-event [:elements/reg-field-keypress-events! field-id]]})))
