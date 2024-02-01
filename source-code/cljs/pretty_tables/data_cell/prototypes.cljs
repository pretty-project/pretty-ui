
(ns pretty-tables.data-cell.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cell-props-prototype
  ; @ignore
  ;
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ;
  ; @return (map)
  [_ cell-props]
  (-> cell-props (pretty-elements.properties/default-font-props    {:font-size :xxs :font-weight :medium})
                 (pretty-elements.properties/default-content-props {})
                 (pretty-elements.properties/default-text-props    {:text-overflow :ellipsis})))
