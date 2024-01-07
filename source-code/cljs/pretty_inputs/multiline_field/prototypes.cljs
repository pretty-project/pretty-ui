
(ns pretty-inputs.multiline-field.prototypes
    (:require [fruits.random.api :as random]
              [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:autofill-name (keyword)
  ;  :max-lines (integer)
  ;  :min-lines (integer)
  ;  :multiline? (boolean)}
  [field-id field-props]
  (merge {:max-lines 32
          :min-lines 1}
         (-> field-props)
         {:multiline? true
          ; XXX#6782 (source-code/cljs/pretty_inputs/text_field/prototypes.cljs)
          :autofill-name (random/generate-keyword)}))
