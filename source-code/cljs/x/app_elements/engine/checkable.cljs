
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.checkable
    (:require [mid-fruits.candy                      :refer [param]]
              [mid-fruits.map                        :as map]
              [x.app-core.api                        :as a :refer [r]]
              [x.app-db.api                          :as db]
              [x.app-elements.engine.element         :as element]
              [x.app-elements.input.events           :as input.events]
              [x.app-elements.input.subs           :as input.subs]
              [x.app-environment.api                 :as environment]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name checkable
;  Olyan input, amelynek a value-path Re-Frame adatbázis útvonalon tárolt értéke
;  true vagy false lehet.
;
; @name checked?
;  Az input {:checked? true} tulajdonsága jelzi, hogy a value-path ... útvonalon
;  tárolt érték boolean típusként kiértékelve igaznak számít.
;
; @name on-check
;  TODO ...
;
; @name on-uncheck
;  TODO ...



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-check-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (function)
  [input-id]
  #(a/dispatch [:elements/input-checked input-id]))

(defn on-uncheck-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (function)
  [input-id]
  #(a/dispatch [:elements/input-unchecked input-id]))

(defn checkable-primary-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:disabled? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:disabled (boolean)
  ;   :on-click (function)
  ;   :on-mouse-up (function)}
  [input-id {:keys [disabled?]}]
  (if disabled? {:disabled true}
                {:on-click     (on-check-function input-id)
                 :on-mouse-up #(environment/blur-element!)}))

(defn checkable-secondary-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:disabled? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:disabled (boolean)
  ;   :on-click (function)
  ;   :on-mouse-up (function)}
  [input-id {:keys [disabled?]}]
  (if disabled? {:disabled true}
                {:on-click     (on-uncheck-function input-id)
                 :on-mouse-up #(environment/blur-element!)}))

(defn checkable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:checked? (boolean)}
  ;
  ; @return (map)
  ;  {:data-checked (boolean)}
  [input-id {:keys [checked?] :as input-props}]
  (element/element-attributes input-id input-props {:data-checked checked?}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; XXX#NEW VERSION!
(defn get-checkable-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [db [_ input-id input-props]]
  (let [value (r input.subs/get-input-value db input-id input-props)]
       (boolean value)))

; XXX#NEW VERSION!
(defn checkable-checked?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [db [_ input-id input-props]]
  (r get-checkable-value db input-id input-props))

; XXX#NEW VERSION!
(defn checkable-nonchecked?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [db [_ input-id input-props]]
  (not (r checkable-checked? db input-id input-props)))



(defn get-checkable-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  ;  {:checked? (boolean)
  ;   :color (keyword)
  ;   :helper (keyword)}
  [db [_ input-id]]
  (merge {:checked? (r checkable-checked? db input-id)}
         (if (r input.subs/required-warning? db input-id)
             {:border-color :warning
              :helper       :please-check-this-field})))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements/input-checked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  (fn [{:keys [db]} [_ input-id]]
      (let [on-check-event (r element/get-element-prop db input-id :on-check)
            value-path     (r element/get-element-prop db input-id :value-path)]
           {:db       (as-> db % (r db/set-item!                 % value-path true)
                                 (r input.events/mark-as-visited! % input-id))
            :dispatch on-check-event})))

(a/reg-event-fx
  :elements/input-unchecked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  (fn [{:keys [db]} [_ input-id]]
      (let [on-uncheck-event (r element/get-element-prop db input-id :on-uncheck)
            value-path       (r element/get-element-prop db input-id :value-path)]
           {:db       (as-> db % (r db/set-item!                 % value-path false)
                                 (r input.events/mark-as-visited! % input-id))
            :dispatch on-uncheck-event})))
