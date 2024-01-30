
(ns pretty-elements.adornment-group.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  [_ group-props]
  (-> group-props (pretty-elements.properties/default-flex-props {:orientation :horizontal :overflow :scroll})
                  (pretty-elements.properties/default-size-props {:height :content :width :content})))
