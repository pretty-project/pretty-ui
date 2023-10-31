
(ns elements.horizontal-group.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {:width (keyword)}
  [group-props]
  (merge {:width :content}
         (-> group-props)))
