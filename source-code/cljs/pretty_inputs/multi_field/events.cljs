
(ns pretty-inputs.multi-field.events
    (:require [fruits.vector.api              :as vector]
              [pretty-inputs.multi-field.subs :as multi-field.subs]
              [re-frame.extra.api             :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn conj-initial-value!
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:value-path (Re-Frame path vector)}
  ; @param (integer) field-dex
  ;
  ; @return (map)
  [db [_ group-id {:keys [value-path]} field-dex]]
  (let [group-value (get-in db value-path)]
       (if (vector/not-empty? group-value)
           (update-in db value-path vector/insert-item (inc field-dex) nil)
           (assoc-in  db value-path [nil nil]))))

(defn decrease-field-count!
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:value-path (Re-Frame path vector)}
  ; @param (integer) field-dex
  ;
  ; @return (map)
  [db [_ group-id {:keys [value-path]} field-dex]]
  (let [group-value         (get-in db value-path)
        updated-group-value (vector/remove-nth-item group-value field-dex)]
       (assoc-in db value-path updated-group-value)))

(defn increase-field-count!
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (map)
  [db [_ group-id group-props field-dex]]
  (if (r multi-field.subs/max-input-count-reached? db group-id group-props)
      (-> db)
      (r conj-initial-value! db group-id group-props field-dex)))
