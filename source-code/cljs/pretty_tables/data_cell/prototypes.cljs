
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
  (-> cell-props (pretty-elements.properties/default-font-props    {:font-size :xxs :font-weight :normal})
                 (pretty-elements.properties/default-content-props {})
                 (pretty-elements.properties/default-flex-props    {:horizontal-align :left :orientation :horizontal})
                 (pretty-elements.properties/default-size-props    {:height :parent :width :parent})
                 (pretty-elements.properties/default-text-props    {:text-overflow :ellipsis})))
