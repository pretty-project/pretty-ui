
(ns pretty-elements.horizontal-polarity.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-props-prototype
  ; @ignore
  ;
  ; @param (map) polarity-props
  ;
  ; @return (map)
  ; {:vertical-align (keyword)
  ;  :width (keyword, px or string)}
  [polarity-props]
  (merge {:vertical-align :center
          :width          :auto}
         (-> polarity-props)))
