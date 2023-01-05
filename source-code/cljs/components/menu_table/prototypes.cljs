
(ns components.menu-table.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-props-prototype
  ; @param (map) table-props
  ;
  ; @return (map)
  ; {:placeholder (metamorphic-content)}
  [table-props]
  (merge {:placeholder :no-items-to-show}
         (param table-props)))
