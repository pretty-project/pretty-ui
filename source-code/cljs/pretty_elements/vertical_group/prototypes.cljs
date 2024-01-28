
(ns pretty-elements.vertical-group.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [group-props]
  (merge {:height :parent
          :width  :auto}
         (-> group-props)))
