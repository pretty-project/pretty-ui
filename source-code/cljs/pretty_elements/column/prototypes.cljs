
(ns pretty-elements.column.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-props-prototype
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ;
  ; @return (map)
  [_ column-props]
  (-> column-props (pretty-elements.properties/default-background-props {})
                   (pretty-elements.properties/default-border-props     {})
                   (pretty-elements.properties/default-content-props    {})
                   (pretty-elements.properties/default-flex-props       {:orientation :vertical :vertical-align :top})))
