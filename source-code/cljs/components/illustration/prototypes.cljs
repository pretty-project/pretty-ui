
(ns components.illustration.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn illustration-props-prototype
  ; @ignore
  ;
  ; @param (map) illustration-props
  ;
  ; @return (map)
  ; {:height (keyword)
  ;  :width (keyword)}
  [illustration-props]
  (merge {:height :xxl
          :width  :xxl}
         (-> illustration-props)))
