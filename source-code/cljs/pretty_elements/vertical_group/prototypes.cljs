
(ns pretty-elements.vertical-group.prototypes
    (:require [pretty-css.api :as pretty-css]))

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
