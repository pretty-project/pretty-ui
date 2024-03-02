
(ns components.menu-table.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-props-prototype
  ; @param (map) table-props
  ;
  ; @return (map)
  ; {:xxx-placeholder (multitype-content)}
  [table-props]
  (merge {:xxx-placeholder :no-items-to-show}
         (-> table-props)))
