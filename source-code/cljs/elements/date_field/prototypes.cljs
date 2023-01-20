
(ns elements.date-field.prototypes
    (:require [random.api :as random]))

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
  ;  :type (keyword)}
  [_ field-props]
  ; XXX#6782 (source-code/cljs/elements/text-field/prototypes.cljs)
  (merge field-props {:autofill-name (random/generate-keyword)
                      :type          :date}))
