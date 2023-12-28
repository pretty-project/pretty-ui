
(ns components.illustration.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn illustration-props-prototype
  ; @ignore
  ;
  ; @param (map) illustration-props
  ;
  ; @return (map)
  ; {:height (keyword, px or string)
  ;  :width (keyword, px or string)}
  [illustration-props]
  (merge {:height :xxl
          :width  :xxl}
         (-> illustration-props)))
