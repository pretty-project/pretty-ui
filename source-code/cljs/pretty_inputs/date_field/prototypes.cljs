
(ns pretty-inputs.date-field.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:type (keyword)}
  [_ field-props]
  (merge field-props {:type :date}))
