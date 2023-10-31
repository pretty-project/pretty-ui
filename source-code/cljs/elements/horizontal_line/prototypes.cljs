
(ns elements.horizontal-line.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn line-props-prototype
  ; @ignore
  ;
  ; @param (map) line-props
  ;
  ; @return (map)
  ; {:fill-color (keyword or string)
  ;  :strength (px)
  ;  :width (keyword)}
  [line-props]
  (merge {:fill-color :default
          :strength   1
          :width      :auto}
         (-> line-props)))
