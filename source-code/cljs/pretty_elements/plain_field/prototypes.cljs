
(ns pretty-elements.plain-field.prototypes
    (:require [pretty-elements.input.utils :as input.utils]
              [noop.api             :refer [return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:field-content-f (function)
  ;  :field-value-f (function)
  ;  :value-path (Re-Frame path vector)}
  [field-id field-props]
  (merge {:field-content-f return
          :field-value-f   return
          :value-path      (input.utils/default-value-path field-id)}
         (-> field-props)))