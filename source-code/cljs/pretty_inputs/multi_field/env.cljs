
(ns pretty-inputs.multi-field.env
    (:require [fruits.vector.api     :as vector]
              [multitype-content.api :as multitype-content]
              [re-frame.extra.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props->single-field?
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (boolean)
  [group-id group-props _]
  (let [group-value @(r/subscribe [:pretty-inputs.multi-field/get-group-value group-id group-props])]
       (vector/item-count? group-value 1)))

(defn group-props->multi-field?
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (boolean)
  [group-id group-props field-dex]
  (let [single-field? (group-props->single-field? group-id group-props field-dex)]
       (not single-field?)))

(defn group-props->min-field-count-reached?
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (boolean)
  [group-id group-props _]
  (let [group-value @(r/subscribe [:pretty-inputs.multi-field/get-group-value group-id group-props])]
       (vector/item-count? group-value 1)))

(defn group-props->max-field-count-reached?
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:max-input-count (integer)}
  ; @param (integer) field-dex
  ;
  ; @return (boolean)
  [group-id {:keys [max-input-count] :as group-props} _]
  (let [group-value @(r/subscribe [:pretty-inputs.multi-field/get-group-value group-id group-props])]
       (vector/item-count? group-value max-input-count)))

(defn field-dex->field-label
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:label (multitype-content)}
  ; @param (integer) field-dex
  ;
  ; @return (multitype-content)
  [group-id {:keys [label] :as group-props} field-dex]
        ; Single-field label
  (cond (and label (group-props->single-field? group-id group-props field-dex))
        (->  label)
        ; Multi-field label
        (and label (group-props->multi-field? group-id group-props field-dex))
        (multitype-content/compose {:content label :suffix (str " #" (inc field-dex))})))

(defn field-dex->control-adornments
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (maps in vector)
  [group-id group-props field-dex]
  [{:icon            :add
    :disabled?       (group-props->max-field-count-reached?              group-id group-props field-dex)
    :on-click        [:pretty-inputs.multi-field/increase-field-count! group-id group-props field-dex]
    :tooltip-content :add-field!}
   {:disabled?       (group-props->min-field-count-reached?              group-id group-props field-dex)
    :icon            :close
    :on-click        [:pretty-inputs.multi-field/decrease-field-count! group-id group-props field-dex]
    :tooltip-content :remove-field!}])

(defn field-dex->end-adornments
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:end-adornments (maps in vector)(opt)}
  ; @param (integer) field-dex
  ;
  ; @return (maps in vector)
  [group-id {:keys [end-adornments] :as group-props} field-dex]
  (vector/concat-items end-adornments (field-dex->control-adornments group-id group-props field-dex)))
