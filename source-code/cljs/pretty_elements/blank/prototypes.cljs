
(ns pretty-elements.blank.prototypes
    (:require [fruits.noop.api :refer [return]]
              [pretty-build-kit.api :as pretty-build-kit]))

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
  (merge {:content-value-f return
          :placeholder-value-f return}
         (-> blank-props)))
