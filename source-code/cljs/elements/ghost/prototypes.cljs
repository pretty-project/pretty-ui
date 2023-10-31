
(ns elements.ghost.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-props-prototype
  ; @ignore
  ;
  ; @param (map) ghost-props
  ;
  ; @return (map)
  ; {:height (keyword)
  ;  :width (keyword)}
  [ghost-props]
  (merge {:height :s
          :width  :auto}
         (-> ghost-props)))
