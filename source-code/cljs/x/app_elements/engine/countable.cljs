
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v0.5.0
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.countable
    (:require [mid-fruits.candy              :refer [param]]
              [x.app-components.api          :as components]
              [x.app-core.api                :as a :refer [r]]
              [x.app-db.api                  :as db]
              [x.app-elements.engine.element :as element]
              [x.app-elements.engine.input   :as input]
              [x.app-environment.api         :as environment]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name countable
;  Olyan input, amelynek a value-path Re-Frame adatbázis útvonalon tárolt értéke
;  egész szám, amelyet a léptető események használatával lehetséges növelni
;  és csökkenteni.
;
; @name changed?
;  Az input {:changed? true} tulajdonsága jelzi, hogy a value-path ... útvonalon
;  tárolt érték nem egyezik meg az input React-fába csatolásának idejében rögzített
;  értékkel.



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-decrease-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (function)
  [input-id]
  #(a/dispatch [:elements/input-decreased input-id]))

(defn on-increase-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (function)
  [input-id]
  #(a/dispatch [:elements/input-increased input-id]))

(defn countable-decrease-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:disabled? (boolean)(opt)
  ;   :min-value (integer)(opt)
  ;   :value (integer)}
  ;
  ; @return (map)
  ;  {:disabled (boolean)
  ;   :on-click (function)}
  [input-id {:keys [disabled? min-value value]}]
  (if (or disabled? (= min-value value))
      {:disabled      true
       :data-disabled true}
      {:on-click     (on-decrease-function input-id)
       :on-mouse-up #(environment/blur-element!)}))

(defn countable-increase-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:disabled? (boolean)(opt)
  ;   :max-value (integer)(opt)
  ;   :value (integer)}
  ;
  ; @return (map)
  ;  {:disabled (boolean)
  ;   :on-click (function)}
  [input-id {:keys [disabled? max-value value]}]
  (if (or disabled? (= max-value value))
      {:disabled      true
       :data-disabled true}
      {:on-click     (on-increase-function input-id)
       :on-mouse-up #(environment/blur-element!)}))

(defn countable-reset-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:changed? (boolean)}
  ;
  ; @return (map)
  ;  {:data-disabled (string)
  ;   :disabled (boolean)
  ;   :on-click (function)
  ;   :title (string)}
  [input-id {:keys [changed?]}]
  (if changed? {:on-click      (input/on-reset-function input-id)
                :on-mouse-up  #(environment/blur-element!)
                :title         (components/content {:content :reset!})}
               {:data-disabled (param true)
                :disabled      (param true)}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- input-decreasable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (let [min-value  (r element/get-element-prop db input-id :min-value)
        value-path (r element/get-element-prop db input-id :value-path)
        value      (get-in db value-path)]
       (or (nil? min-value)
           (and (some? min-value)
                (>     min-value value)))))

(defn- input-increasable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (let [max-value  (r element/get-element-prop db input-id :max-value)
        value-path (r element/get-element-prop db input-id :value-path)
        value      (get-in db value-path)]
       (or (nil? max-value)
           (and (some? max-value)
                (>     max-value value)))))

(defn get-countable-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  ;  {:changed? (boolean)
  ;   :value (integer)}
  [db [_ input-id]]
  {:changed? (r input/input-value-changed? db input-id)
   :value    (r input/get-input-value      db input-id)})



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements/input-decreased
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  (fn [{:keys [db]} [_ input-id]]
    (let [value-path (r element/get-element-prop db input-id :value-path)]
         (if (r input-decreasable? db input-id)
             {:db (r db/apply! db value-path dec)}))))

(a/reg-event-fx
  :elements/input-increased
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  (fn [{:keys [db]} [_ input-id]]
      (let [value-path (r element/get-element-prop db input-id :value-path)]
           (if (r input-increasable? db input-id)
               {:db (r db/apply! db value-path inc)}))))
