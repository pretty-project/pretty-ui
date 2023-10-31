
(ns components.menu-table.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-props-prototype
  ; @param (map) table-props
  ;
  ; @return (map)
  ; {:placeholder (metamorphic-content)}
  [table-props]
  (merge {:placeholder :no-items-to-show}
         (-> table-props)))
