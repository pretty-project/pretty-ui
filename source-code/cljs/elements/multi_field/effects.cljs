
(ns elements.multi-field.effects
    (:require [elements.multi-field.events  :as multi-field.events]
              [elements.multi-field.helpers :as multi-field.helpers]
              [re-frame.api                 :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.multi-field/decrease-field-count!
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  (fn [{:keys [db]} [_ group-id group-props field-dex]]
      {:db (r multi-field.events/decrease-field-count! db group-id group-props field-dex)}))

(r/reg-event-fx :elements.multi-field/increase-field-count!
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  (fn [{:keys [db]} [_ group-id group-props field-dex]]
      (let [field-id (multi-field.helpers/field-dex->field-id group-id group-props (inc field-dex))]
           {:db (r multi-field.events/increase-field-count! db group-id group-props field-dex)
            :dispatch-later [{:ms 100 :fx [:elements.plain-field/focus-field! field-id]}]})))
