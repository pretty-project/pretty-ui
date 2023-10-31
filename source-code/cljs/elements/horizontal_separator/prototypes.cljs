
(ns elements.horizontal-separator.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-props-prototype
  ; @ignore
  ;
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:color (keyword)
  ;  :width (keyword)}
  [separator-props]
  (merge {:color :default
          :width :auto}
         (-> separator-props)))
