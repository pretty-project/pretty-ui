
(ns pretty-elements.vertical-line.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn line-props-prototype
  ; @ignore
  ;
  ; @param (map) line-props
  ;
  ; @return (map)
  ; {:fill-color (keyword or string)
  ;  :height (keyword)
  ;  :strength (px)}
  [line-props]
  (merge {:fill-color :default
          :height     :parent
          :strength   1}
         (-> line-props)))
