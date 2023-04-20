
(ns components.menu-table.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-props-prototype
  ; @param (map) table-props
  ;
  ; @return (map)
  ; {:placeholder (metamorphic-content)}
  [table-props]
  (merge {:placeholder "No items to show"}
         (param table-props)))
