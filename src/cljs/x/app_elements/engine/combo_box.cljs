
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.02
; Description:
; Version: v0.9.8
; Compatibility: x4.3.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.combo-box
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.engine.element     :as element]
              [x.app-elements.engine.field       :as field]
              [x.app-elements.engine.input       :as input]
              [x.app-elements.engine.input-group :as input-group]
              [x.app-elements.engine.surface     :as surface]))



;; -- Combo box ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A combo-box elem összetettségéből adódóan saját névteret (eszközkészletet)
; igényel, mivel az x.app-elements.engine.field eszközök normál szövegmező
; készítéséhez alkalmasak.



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn key-code->keypress-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (integer) key-code
  ;
  ; @example
  ;  (key-code->keypress-id :my-field 40)
  ;  => :my-field--40
  ;
  ; @return (keyword)
  [field-id key-code]
  (keyword/join field-id "--" key-code))

(defn view-props->render-option?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:get-label-f (function)
  ;   :value (string)}
  ;
  ; @return (boolean)
  [{:keys [get-label-f value]} option]
  (string/starts-with? (get-label-f option)
                       (param       value)
                       {:case-sensitive? false}))

(defn view-props->rendered-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:options (vector)}
  ;
  ; @return (vector)
  [{:keys [options] :as view-props}]
  (reduce (fn [rendered-options option]
              (if (view-props->render-option? view-props option)
                  (vector/conj-item rendered-options option)
                  (return rendered-options)))
          (param [])
          (param options)))

(defn view-props->value-extendable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:get-label-f (function)
  ;   :options (maps in vector)
  ;   :value (keyword)(opt)}
  ;
  ; @example
  ;  (combo-box/view-props->value-extendable?
  ;    {:get-label-f #(get % :label)
  ;     :options     [{:label "Apple juice"   :value "apple-juice"}
  ;                   {:label "Avocado juice" :value "avocado-juice"}]
  ;     :value       "apple-juice"})
  ;  => false
  ;
  ; @example
  ;  (combo-box/view-props->value-extendable?
  ;    {:get-label-f #(get % :label)
  ;     :options     [{:label "Apple juice"   :value "apple-juice"}
  ;                   {:label "Avocado juice" :value "avocado-juice"}]
  ;     :value       "mango-juice"})
  ;  => true
  ;
  ; @return (boolean)
  [{:keys [get-label-f options value]}]
  (not (vector/any-item-match? options #(= value (get-label-f %1)))))

(defn view-props->render-extender?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:extendable? (boolean)
  ;   :value (keyword)}
  ;
  ; @return (boolean)
  [{:keys [extendable? value] :as view-props}]
  (and (boolean                       extendable?)
       (string/nonempty?              value)
       (view-props->value-extendable? view-props)))

(defn view-props->render-options?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:options (* in vector)}
  ;
  ; @return (boolean)
  [{:keys [options] :as view-props}]
  (and (vector/nonempty? options)
       (vector/any-item-match? options #(view-props->render-option? view-props %1))))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-highlighted-option-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (integer)
  [db [_ field-id]]
  (r element/get-element-subprop db field-id [:surface-props :highlighted-option]))

(defn any-option-highlighted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (integer)
  [db [_ field-id]]
  (let [highlighted-option-dex (r get-highlighted-option-dex db field-id)]
       (vector/item-dex? highlighted-option-dex)))

(defn get-combo-box-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (string)
  [db [_ field-id]]
  (if-let [value (r input/get-input-value db field-id)]
          (if (string? value)
              (return value)
              (let [get-label-f (r element/get-element-prop db field-id :get-label-f)]
                   (get-label-f value)))))

(defn combo-box-filled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (string)
  [db [_ field-id]]
  (let [combo-box-value (r get-combo-box-value db field-id)]
       (string/nonempty? combo-box-value)))

(defn combo-box-empty?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (string)
  [db [_ field-id]]
  (let [combo-box-filled? (r combo-box-filled? db field-id)]
       (not combo-box-filled?)))

(defn get-combo-box-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (vector)
  [db [_ field-id]]
  (let [options-path (r element/get-element-prop db field-id :options-path)]
       (get-in db options-path)))

(defn render-combo-box-option?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (*) option
  ;
  ; @return (boolean)
  [db [_ field-id option]]
  ; A combo-box elem React-fába csatolása után van olyan pillanat, amikor az elem
  ; feliratkozása már aktív, de az elem tulajdonságai még nem elérhetők
  ; a Re-Frame adatbázisban. És a get-label-f értéke nem lehet nil!
  (if-let [get-label-f (r element/get-element-prop db field-id :get-label-f)]
          (let [value (r get-combo-box-value db field-id)]
               (string/starts-with? (get-label-f option)
                                    (param       value)
                                    {:case-sensitive? false}))))

(defn get-combo-box-rendered-option-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (*) option
  ; @param (integer) render-dex
  ;
  ; @return (map)
  ;  {:option (*)
  ;   :highlighted? (boolean)
  ;   :rendered? (boolean)}
  [db [_ field-id option render-dex]]
  (let [highlighted-option-dex (r get-highlighted-option-dex db field-id)
        option-highlighted?    (= render-dex highlighted-option-dex)]
       {:option       option
        :highlighted? option-highlighted?
        :rendered?    true}))

(defn get-combo-box-non-rendered-option-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (*) option
  ;
  ; @return (map)
  ;  {:option (*)
  ;   :rendered? (boolean)}
  [db [_ _ option]]
  {:option    option
   :rendered? false})

(defn get-combo-box-option-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (*) option
  ; @param (integer) render-dex
  ;
  ; @return (map)
  [db [_ field-id option render-dex]]
  (if-let [option-rendered? (r render-combo-box-option? db field-id option)]
          (r get-combo-box-rendered-option-data     db field-id option render-dex)
          (r get-combo-box-non-rendered-option-data db field-id option)))

(defn get-combo-box-rendered-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A combo-box elem option listájában megjelenített értékeket a Re-Frame
  ; feliratkozásban szükséges szűrni, hogy az [:x.app-elements/highlight-prev-option! ...]
  ; és [:x.app-elements/highlight-next-option! ...] események is ki tudják olvasni
  ; a Re-Frame adatbázisból, hogy melyik értékek vannak és hány érték kerül kirenderelésre.
  ;
  ; @param (keyword) field-id
  ;
  ; @return (maps in vector)
  ;  [{:option (*)
  ;    :highlighted? (boolean)
  ;    :rendered? (boolean)
  ;    :selected? (boolean)}]
  [db [_ field-id]]
  (let [options (r get-combo-box-options db field-id)]
       (reduce (fn [rendered-options option]
                   ; render-dex: A combo-box elem surface felületén hányadik elemként
                   ; kerül kirenderelésre az opció (az opciók listájának szűrése után)
                   (let [render-dex  (count rendered-options)
                         option-data (r get-combo-box-option-data db field-id option render-dex)]
                        (if (get option-data :rendered?)
                            (vector/conj-item rendered-options option-data)
                            (return           rendered-options))))
               (param [])
               (param options))))

(defn any-combo-box-option-rendered?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [db [_ field-id]]
  (let [rendered-options (r get-combo-box-rendered-options db field-id)]
       (vector/nonempty? rendered-options)))

(defn get-highlighted-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (*)
  [db [_ field-id]]
  (if-let [highlighted-option-dex (r get-highlighted-option-dex db field-id)]
          (let [rendered-options        (r get-combo-box-rendered-options db field-id)
                highlighted-option-data (vector/nth-item rendered-options highlighted-option-dex)]
               (get highlighted-option-data :option))))

(defn get-combo-box-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  ;  {:field-empty? (boolean)
  ;   :options (vector)
  ;   :value (string)}
  [db [_ field-id]]
   ; XXX#8093
   ; A {:field-empty? ...} tulajdonság használatával állapítja meg a text-field
   ; elem, hogy melyik adornment gombot szükséges megjeleníteni.
  {:field-empty?     (r combo-box-empty?               db field-id)
   :options          (r get-combo-box-options          db field-id)
   :rendered-options (r get-combo-box-rendered-options db field-id)
   :value            (r get-combo-box-value            db field-id)})



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn discard-option-highlighter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (r element/remove-element-subprop! db field-id [:surface-props :highlighted-option]))

(defn use-highlighted-combo-box-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (let [highlighted-option       (r get-highlighted-option   db field-id)
        get-label-f              (r element/get-element-prop db field-id :get-label-f)
        highlighted-option-label (get-label-f highlighted-option)]
       (r field/set-field-value! db field-id highlighted-option-label)))

(defn use-highlighted-multi-combo-box-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (let [group-id           (r input-group/get-input-group-id db field-id)
        highlighted-option (r get-highlighted-option         db field-id)]
       (r input-group/stack-to-group-value! db group-id field-id highlighted-option)))

(defn up-combo-box!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (cond (r surface/surface-hidden? db field-id)
        (r surface/show-surface!   db field-id)
        (r any-combo-box-option-rendered? db field-id)
        (let [rendered-options       (r get-combo-box-rendered-options db field-id)
              highlighted-option-dex (r get-highlighted-option-dex db field-id)
              prev-option-dex        (vector/prev-dex rendered-options highlighted-option-dex)]
             (r element/set-element-subprop! db field-id [:surface-props :highlighted-option] prev-option-dex))
        :else (r element/remove-element-subprop! db field-id [:surface-props :highlighted-option])))

(a/reg-event-db :x.app-elements/up-combo-box! up-combo-box!)

(defn down-combo-box!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (cond (r surface/surface-hidden? db field-id)
        (r surface/show-surface!   db field-id)
        (r any-combo-box-option-rendered? db field-id)
        (let [highlighted-option-dex (r get-highlighted-option-dex db field-id)
              highlighted-option-dex (or highlighted-option-dex    -1)
              rendered-options       (r get-combo-box-rendered-options db field-id)
              next-option-dex        (vector/next-dex rendered-options highlighted-option-dex)]
             (r element/set-element-subprop! db field-id [:surface-props :highlighted-option] next-option-dex))
        :else (r element/remove-element-subprop! db field-id [:surface-props :highlighted-option])))

(a/reg-event-db :x.app-elements/down-combo-box! down-combo-box!)

(defn enter-combo-box!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [event-id field-id]]
  (if (r any-option-highlighted? db field-id)
      (-> db (use-highlighted-combo-box-option! [event-id field-id])
             (discard-option-highlighter!       [event-id field-id])
             (surface/hide-surface!             [event-id field-id]))
      (-> db (discard-option-highlighter!       [event-id field-id])
             (surface/hide-surface!             [event-id field-id]))))

(a/reg-event-db :x.app-elements/enter-combo-box! enter-combo-box!)

(defn enter-multi-combo-box!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [event-id field-id]]
  (if (r any-option-highlighted? db field-id)
      (-> db (use-highlighted-multi-combo-box-option! [event-id field-id])
             (discard-option-highlighter!             [event-id field-id])
             (surface/hide-surface!                   [event-id field-id]))
      (-> db (discard-option-highlighter!             [event-id field-id])
             (surface/hide-surface!                   [event-id field-id]))))

(a/reg-event-db :x.app-elements/enter-multi-combo-box! enter-multi-combo-box!)

(defn escape-combo-box!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (if-let [highlighted-option (r get-highlighted-option db field-id)]
          (r discard-option-highlighter! db field-id)
          (r surface/hide-surface!       db field-id)))

(a/reg-event-db :x.app-elements/escape-combo-box! escape-combo-box!)



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ->combo-box-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (r element/remove-element-subprop! db field-id [:surface-props :highlighted-option]))

(a/reg-event-db :x.app-elements/->combo-box-changed ->combo-box-changed)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-elements/reg-combo-box-controllers!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [_ [_ field-id]]
      {:dispatch-n [[:x.app-environment.keypress-handler/reg-keypress-event!
                     (key-code->keypress-id field-id 40)
                     {:key-code 40 :on-keydown [:x.app-elements/down-combo-box! field-id]
                      :prevent-default? true}]
                    [:x.app-environment.keypress-handler/reg-keypress-event!
                     (key-code->keypress-id field-id 38)
                     {:key-code 38 :on-keydown [:x.app-elements/up-combo-box! field-id]
                      :prevent-default? true}]
                    [:x.app-environment.keypress-handler/reg-keypress-event!
                     (key-code->keypress-id field-id 27)
                     {:key-code 27 :on-keydown [:x.app-elements/escape-combo-box! field-id]}]
                    [:x.app-environment.keypress-handler/reg-keypress-event!
                     (key-code->keypress-id field-id 13)
                     {:key-code 13 :on-keydown [:x.app-elements/enter-combo-box! field-id]}]]}))

(a/reg-event-fx
  :x.app-elements/reg-multi-combo-box-controllers!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [_ [_ field-id]]
      {:dispatch-n [[:x.app-environment.keypress-handler/reg-keypress-event!
                     (key-code->keypress-id field-id 40)
                     {:key-code 40 :on-keydown [:x.app-elements/down-combo-box! field-id]
                      :prevent-default? true}]
                    [:x.app-environment.keypress-handler/reg-keypress-event!
                     (key-code->keypress-id field-id 38)
                     {:key-code 38 :on-keydown [:x.app-elements/up-combo-box! field-id]
                      :prevent-default? true}]
                    [:x.app-environment.keypress-handler/reg-keypress-event!
                     (key-code->keypress-id field-id 27)
                     {:key-code 27 :on-keydown [:x.app-elements/escape-combo-box! field-id]}]
                    [:x.app-environment.keypress-handler/reg-keypress-event!
                     (key-code->keypress-id field-id 13)
                     {:key-code 13 :on-keydown [:x.app-elements/enter-multi-combo-box! field-id]}]]}))

(a/reg-event-fx
  :x.app-elements/remove-combo-box-controllers!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  (fn [_ [_ field-id]]
      {:dispatch-n [[:x.app-environment.keypress-handler/remove-keypress-event!
                     (key-code->keypress-id field-id 40)]
                    [:x.app-environment.keypress-handler/remove-keypress-event!
                     (key-code->keypress-id field-id 38)]
                    [:x.app-environment.keypress-handler/remove-keypress-event!
                     (key-code->keypress-id field-id 27)]
                    [:x.app-environment.keypress-handler/remove-keypress-event!
                     (key-code->keypress-id field-id 13)]]}))
