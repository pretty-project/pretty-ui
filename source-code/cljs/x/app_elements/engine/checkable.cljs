
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v0.6.8
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.checkable
    (:require [mid-fruits.candy                 :refer [param]]
              [mid-fruits.map                   :as map]
              [x.app-core.api                   :as a :refer [r]]
              [x.app-db.api                     :as db]
              [x.app-elements.engine.element    :as element]
              [x.app-elements.engine.input      :as input]
              [x.app-elements.engine.focusable  :as focusable]
              [x.app-elements.engine.targetable :as targetable]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-check-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (function)
  [input-id]
  #(a/dispatch [:elements/->input-checked input-id]))

(defn on-uncheck-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (function)
  [input-id]
  #(a/dispatch [:elements/->input-unchecked input-id]))

(defn checkable-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:checked? (boolean)
  ;   :disabled? (boolean)(opt)
  ;   :targetable? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:disabled (boolean)
  ;   :id (string)
  ;   :on-click (function)}
  [input-id {:keys [checked? disabled? targetable?]}]
  (cond-> (param {})
          (boolean disabled?)   (merge {:disabled true})
          (boolean checked?)    (merge {:on-click    (on-uncheck-function             input-id)
                                        :on-mouse-up (focusable/blur-element-function input-id)})
          (not     checked?)    (merge {:on-click    (on-check-function               input-id)
                                        :on-mouse-up (focusable/blur-element-function input-id)})
          (boolean targetable?) (merge {:id (targetable/element-id->target-id input-id)})))

(defn checkable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) view-props
  ;  {:checked? (boolean)}
  ;
  ; @return (map)
  ;  {:data-checked (boolean)}
  [input-id {:keys [checked?] :as view-props}]
  (element/element-attributes input-id view-props {:data-checked checked?}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-checkable-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (let [value (r input/get-input-value db input-id)]
       (boolean value)))

(defn checkable-checked?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (r get-checkable-value db input-id))

(defn checkable-nonchecked?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (not (r checkable-checked? db input-id)))

(defn get-checkable-view-props
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
         (if (r input/input-required-warning? db input-id)
             {:color  :warning
              :helper :please-check-this-field})))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements/->input-checked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  (fn [{:keys [db]} [_ input-id]]
      (let [on-check-event (r element/get-element-prop db input-id :on-check)
            value-path     (r element/get-element-prop db input-id :value-path)]
           {:db (as-> db % (r db/set-item!                 % value-path true)
                           (r input/mark-input-as-visited! % input-id))
            :dispatch on-check-event})))

(a/reg-event-fx
  :elements/->input-unchecked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  (fn [{:keys [db]} [_ input-id]]
      (let [on-uncheck-event (r element/get-element-prop db input-id :on-uncheck)
            value-path       (r element/get-element-prop db input-id :value-path)]
           {:db (as-> db % (r db/set-item!                 % value-path false)
                           (r input/mark-input-as-visited! % input-id))
            :dispatch on-uncheck-event})))
