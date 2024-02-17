
(ns pretty-inputs.multiline-field.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:max-lines (integer)
  ;  :min-lines (integer)
  ;  :multiline? (boolean)}
  [field-props]
  (merge {:max-lines 32
          :min-lines 1}
         (-> field-props)
         {:multiline? true}))

         ; (pretty-properties/default-content-props {:content (env/get-field-content)}) ; <- auto-count-content-lines miatt kell
