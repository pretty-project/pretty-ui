
(ns pretty-elements.vertical-polarity.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-props-prototype
  ; @ignore
  ;
  ; @param (map) polarity-props
  ;
  ; @return (map)
  ; {:height (keyword, px or string)
  ;  :horizontal-align (keyword)}
  [polarity-props]
  (merge {:height           :parent
          :horizontal-align :center}
         (-> polarity-props)))
