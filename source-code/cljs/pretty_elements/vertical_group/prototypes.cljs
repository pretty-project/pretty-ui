
(ns pretty-elements.vertical-group.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [group-props]
  (merge {:height :parent
          :width  :auto}
         (-> group-props)))
