
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.01
; Description:
; Version: v0.3.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.collectable
    (:require [mid-fruits.candy                 :refer [param]]
              [mid-fruits.map                   :as map]
              [mid-fruits.vector                :as vector]
              [x.app-core.api                   :as a :refer [r]]
              [x.app-db.api                     :as db]
              [x.app-elements.engine.element    :as element]
              [x.app-elements.engine.input      :as input]
              [x.app-elements.engine.focusable  :as focusable]
              [x.app-elements.engine.targetable :as targetable]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-collect-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (keyword) option-id
  ;
  ; @return (function)
  [input-id option-id]
  #(a/dispatch-n [[:elements/->option-collected input-id option-id]]))

(defn on-uncollect-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (keyword) option-id
  ;
  ; @return (function)
  [input-id option-id]
  #(a/dispatch [:elements/->option-uncollected input-id option-id]))

(defn collectable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:value (boolean)}
  ;
  ; @return (map)
  [input-id {:keys [value] :as input-props}]
  (element/element-attributes input-id input-props))

(defn collectable-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:disabled? (boolean)(opt)
  ;   :targetable? (boolean)(opt)
  ;   :value (keyword)(opt)}
  ; @param (map) option-props
  ;  {:id (keyword)}
  ;
  ; @return (map)
  ;  {:disabled (boolean)
  ;   :id (string)
  ;   :on-click (function)}
  [input-id {:keys [disabled? targetable? value]} {:keys [id]}]
  (cond-> {} (boolean disabled?) (merge {:disabled true})
             (not     disabled?) (merge {:on-click    (on-collect-function             input-id id)
                                         ; WARNING! NOT TESTED!
                                         :on-mouse-up (focusable/blur-element-function input-id)})
                                         ; WARNING! NOT TESTED!
             (and (not     disabled?)
                  (boolean targetable?)) (merge {:id (targetable/element-id->target-id input-id)})))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn collectable-collected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (let [value (r input/get-input-value db input-id)]
       (some? value)))

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
  ;   :value (vector)}
  [db [_ input-id]]
  (merge {:collected? (r collectable-collected? db input-id)
          :value      (r input/get-input-value  db input-id)}
         (if (r input/input-required-warning? db input-id)
             {:color  :warning
              :helper :please-select-an-option})))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements/->option-collected
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (keyword) option-id
  (fn [{:keys [db]} [_ input-id option-id]]
      (let [on-collect-event (r element/get-element-prop db input-id :on-collect)
            value-path       (r element/get-element-prop db input-id :value-path)]
           {:db (as-> db % (r db/apply!                    % value-path vector/conj-item option-id)
                           (r input/mark-input-as-visited! % input-id))
            :dispatch on-collect-event})))

(a/reg-event-fx
  :elements/->option-uncollected
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (keyword) option-id
  (fn [{:keys [db]} [_ input-id option-id]]
      (let [value-path (r element/get-element-prop db input-id :value-path)]
           {:db (r db/apply! db value-path vector/remove-item option-id)})))
