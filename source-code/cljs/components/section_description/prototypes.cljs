
(ns components.section-description.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn description-props-prototype
  ; @param (map) description-props
  ;
  ; @return (map)
  ; {:color (keyword)
  ;  :font-size (keyword, px or string)
  ;  :horizontal-align (keyword)
  ;  :indent (map)
  ;  :line-height (keyword)}
  [description-props]
  (merge {:color            :muted
          :font-size        :xxs
          :horizontal-align :center
          :indent           {:horizontal :xs :vertical :xs}}
         (-> description-props)))
