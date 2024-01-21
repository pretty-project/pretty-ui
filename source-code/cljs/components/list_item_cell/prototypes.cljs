
(ns components.list-item-cell.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cell-props-prototype
  ; @param (map) cell-props
  ;
  ; @return (map)
  [_])

(defn row-props-prototype
  ; @param (map) cell-props
  ;
  ; @return (map)
  [cell-props]
  (merge {:indent {:vertical :xs}
          :text-selectable? true}
         (-> cell-props)))
