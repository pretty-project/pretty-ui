
(ns components.data-table.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-props-prototype
  ; @param (map) table-props
  ;
  ; @return (map)
  ; {:font-size (keyword)}
  [table-props]
  (merge {:font-size :s}
         (param table-props)))
