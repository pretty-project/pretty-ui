
(ns components.spinner.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn spinner-props-prototype
  ; @param (map) spinner-props
  ;
  ; @return (map)
  ; {:color (keyword)
  ;  :size (keyword)}
  [spinner-props]
  (merge {:color :primary
          :size  :m}
         (-> spinner-props)))
