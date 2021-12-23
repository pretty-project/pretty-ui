
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.01
; Description:
; Version: v0.4.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.collectable
    (:require [mid-fruits.candy                 :refer [param]]
              [mid-fruits.vector                :as vector]
              [x.app-core.api                   :as a :refer [r]]
              [x.app-db.api                     :as db]
              [x.app-elements.engine.element    :as element]
              [x.app-elements.engine.input      :as input]
              [x.app-elements.engine.focusable  :as focusable]
              [x.app-elements.engine.selectable :as selectable]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name collectable
;  Olyan input, amelynek a value-path Re-Frame adatbázis útvonalon tárolt értéke
;  egy vektor, amelyben a kiválasztható opciók értékei gyűjthetők.
;  Az egyes opciók kiválasztásakor azokból a {:get-value-f ...} függvénnyel
;  vonja ki azok értékekét.
;  Egymással megegyező értékből egyszerre több nem tárolódik a gyűjteményben!
;
; @name collected?
;  Az input {:collected? true} tulajdonsága jelzi, hogy a value-path ... útvonalon
;  tárolt érték egy nem üres vektor.



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-collect-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  ;
  ; @return (function)
  [input-id option]
  #(a/dispatch [:elements/collect-option! input-id option]))

(defn on-uncollect-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  ;
  ; @return (function)
  [input-id option]
  #(a/dispatch [:elements/uncollect-option! input-id option]))

(defn on-toggle-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  ;
  ; @return (function)
  [input-id option]
  #(a/dispatch [:elements/toggle-option-collection! input-id option]))

(defn input-props->option-collected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) input-props
  ;  {:collected-value (vector)
  ;   :get-value-f (function)}
  ; @param (*) option
  ;
  ; @return (boolean)
  [{:keys [collected-value get-value-f]} option]
  (let [value (get-value-f option)]
       (vector/contains-item? collected-value value)))

(defn collectable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (map)
  [input-id {:keys [] :as input-props}]
  (element/element-attributes input-id input-props))

(defn collectable-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:disabled? (boolean)(opt)}
  ; @param (*) option
  ;
  ; @return (map)
  ;  {:data-collected (boolean)
  ;   :disabled (boolean)
  ;   :on-click (function)
  ;   :on-mouse-up (function)}
  [input-id {:keys [disabled?] :as input-props} option]
  (if disabled? {:disabled true}
                {:data-collected (input-props->option-collected?  input-props option)
                 :on-click       (on-toggle-function              input-id option)
                 :on-mouse-up    (focusable/blur-element-function input-id)}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-collectable-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [db [_ input-id]]
  ; BUG#7633
  (if-let [options-path (r element/get-element-prop db input-id :options-path)]
          (get-in db options-path)))

(defn get-collected-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [db [_ input-id]]
  (vec (r input/get-input-value db input-id)))







(defn option-collected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  ;
  ; @return (boolean)
  [db [_ input-id option]]
  (let [get-value-f     (r element/get-element-prop db input-id :get-value-f)
        value-path      (r element/get-element-prop db input-id :value-path)
        collected-value (r get-collected-value      db input-id)
        value           (get-value-f option)]
       (vector/contains-item? collected-value value)))

(defn collectable-collected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (let [collected-value (r get-collected-value db input-id)]
       (vector/nonempty? collected-value)))

(defn collectable-noncollected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (not (r collectable-collected? db input-id)))

(defn get-collectable-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  ;  {:collected? (boolean)
  ;   :color (keyword)
  ;   :helper (keyword)
  ;   :collected-value (vector)}
  [db [_ input-id]]
  (merge {:collected?      (r collectable-collected?  db input-id)
          :collected-value (r get-collected-value     db input-id)
          :options         (r get-collectable-options db input-id)}
         (if (r input/input-required-warning? db input-id)
             {:border-color :warning
              :helper       :please-select-an-option})))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-collectable!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (r selectable/init-selectable! db input-id))

(a/reg-event-db :elements/init-collectable! init-collectable!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements/collect-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  (fn [{:keys [db]} [_ input-id option]]
      (let [on-collect-event (r element/get-element-prop db input-id :on-collect)
            get-value-f      (r element/get-element-prop db input-id :get-value-f)
            value-path       (r element/get-element-prop db input-id :value-path)
            value            (get-value-f option)]
           {:db (as-> db % (r db/apply!                    % value-path vector/conj-item-once value)
                           (r input/mark-input-as-visited! % input-id))
            :dispatch on-collect-event})))

(a/reg-event-fx
  :elements/uncollect-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  (fn [{:keys [db]} [_ input-id option]]
      (let [on-uncollect-event (r element/get-element-prop db input-id :on-uncollect)
            value-path         (r element/get-element-prop db input-id :value-path)
            get-value-f        (r element/get-element-prop db input-id :get-value-f)
            value              (get-value-f option)]
           {:db (r db/apply! db value-path vector/remove-item value)
            :dispatch on-uncollect-event})))

(a/reg-event-fx
  :elements/toggle-option-collection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  (fn [{:keys [db]} [_ input-id option]]
      (if (r option-collected? db input-id option)
          [:elements/uncollect-option! input-id option]
          [:elements/collect-option!   input-id option])))
