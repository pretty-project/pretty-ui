
(ns pretty-tables.data-cell.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

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
  (-> cell-props (pretty-properties/default-font-props    {:font-size :xxs :font-weight :normal})
                 (pretty-properties/default-content-props {})
                 (pretty-properties/default-flex-props    {:horizontal-align :left :orientation :horizontal})
                 (pretty-properties/default-size-props    {:height :parent :width :parent})
                 (pretty-properties/default-text-props    {:text-overflow :ellipsis})))
