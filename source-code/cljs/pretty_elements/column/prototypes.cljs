
(ns pretty-elements.column.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

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
  (-> column-props (pretty-properties/default-background-props {})
                   (pretty-properties/default-border-props     {})
                   (pretty-properties/default-content-props    {})
                   (pretty-properties/default-flex-props       {:orientation :vertical :vertical-align :top})
                   (pretty-properties/default-size-props       {})))
