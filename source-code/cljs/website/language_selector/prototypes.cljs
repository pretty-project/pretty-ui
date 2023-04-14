
(ns website.language-selector.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-props-prototype
  ; @ignore
  ;
  ; @param (map) selector-props
  ;
  ; @return (map)
  ; {:font-size (keyword)}
  ; {:gap (keyword)}
  [selector-props]
  (merge {:font-size :s
          :gap       :xxs}
         (param selector-props)))
