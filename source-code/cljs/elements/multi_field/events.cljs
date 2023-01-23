
(ns elements.multi-field.events
    (:require [elements.multi-field.subs :as multi-field.subs]
              [noop.api                  :refer [return]]
              [re-frame.api              :refer [r]]
              [vector.api                :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn conj-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:value-path (vector)}
  ; @param (integer) field-dex
  ;
  ; @return (map)
  [db [_ group-id {:keys [value-path]} field-dex]]
  (let [group-value (get-in db value-path)]
       (if (vector/nonempty? group-value)
           (update-in db value-path vector/inject-item (inc field-dex) nil)
           (assoc-in  db value-path [nil nil]))))

(defn decrease-field-count!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:value-path (vector)}
  ; @param (integer) field-dex
  ;
  ; @return (map)
  [db [_ group-id {:keys [value-path]} field-dex]]
  (let [group-value         (get-in db value-path)
        updated-group-value (vector/remove-nth-item group-value field-dex)]
       (assoc-in db value-path updated-group-value)))

(defn increase-field-count!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (map)
  [db [_ group-id group-props field-dex]]
  (if (r multi-field.subs/max-input-count-reached? db group-id group-props)
      (return db)
      (r conj-initial-value! db group-id group-props field-dex)))
