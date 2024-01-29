
(ns pretty-elements.vertical-group.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [_ group-props]
  (merge {:height :parent
          :width  :auto}
         (-> group-props)))
