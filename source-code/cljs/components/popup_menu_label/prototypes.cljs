
(ns components.popup-menu-label.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:color (keyword)
  ;  :gap (keyword, px or string)
  ;  :horizontal-align (keyword)
  ;  :icon-size (keyword)
  ;  :indent (map)}
  [label-props]
  (merge {:color            :muted
          :gap              :xs
          :horizontal-align :left
          :icon-size        :m
          :indent           {:horizontal :xxs :vertical :s}}
         (-> label-props)))
