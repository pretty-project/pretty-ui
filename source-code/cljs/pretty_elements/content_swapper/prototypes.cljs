
(ns pretty-elements.content-swapper.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn swapper-props-prototype
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ;
  ; @return (map)
  [_ swapper-props]
  (-> swapper-props (pretty-elements.properties/default-animation-props {})
                    (pretty-elements.properties/default-content-props   {})
                    (pretty-elements.properties/default-size-props      {:height :m :width :m})))
