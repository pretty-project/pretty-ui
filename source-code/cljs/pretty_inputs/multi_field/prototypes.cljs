
(ns pretty-inputs.multi-field.prototypes
    (:require [pretty-inputs.multi-field.env   :as multi-field.env]
              [pretty-inputs.multi-field.utils :as multi-field.utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {:max-input-count (integer)
  ;  :value-path (Re-Frame path vector)}
  [group-id group-props]
  (merge {:max-input-count 8}
          ;:value-path (input.utils/default-value-path group-id)}
         (-> group-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (map)
  ; {}
  [group-id group-props field-dex]
  (merge (dissoc group-props :indent :outdent :style)
         {:autofocus?     (multi-field.utils/field-dex->autofocus?   group-id group-props field-dex)
          :end-adornments (multi-field.env/field-dex->end-adornments group-id group-props field-dex)
          :label          (multi-field.env/field-dex->field-label    group-id group-props field-dex)
          :value-path     (multi-field.utils/field-dex->value-path   group-id group-props field-dex)}))
