
(ns pretty-elements.blank.prototypes
    (:require [fruits.noop.api :refer [return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-props-prototype
  ; @ignore
  ;
  ; @param (map) blank-props
  ;
  ; @return (map)
  ; {:content-value-f (function)}
  [blank-props]
  (merge {:content-value-f return}
         (-> blank-props)))
