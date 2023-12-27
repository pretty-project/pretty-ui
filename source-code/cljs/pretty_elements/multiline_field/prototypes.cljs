
(ns pretty-elements.multiline-field.prototypes
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
  ;  :max-height (integer)
  ;  :min-height (integer)
  ;  :multiline? (boolean)}
  [field-id field-props]
  (merge {:max-height 32
          :min-height 1}
         (-> field-props)
         {:multiline? true
          ; XXX#6782 (source-code/cljs/pretty_elements/text_field/prototypes.cljs)
          :autofill-name (random/generate-keyword)}))
