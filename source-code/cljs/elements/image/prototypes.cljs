
(ns elements.image.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-props-prototype
  ; @ignore
  ;
  ; @param (map) image-props
  ;
  ; @return (map)
  ; {}
  [image-props]
  (merge {:height :content
          :width  :content}
         (-> image-props)))
