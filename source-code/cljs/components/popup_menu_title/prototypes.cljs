
(ns components.popup-menu-title.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn title-props-prototype
  ; @param (map) title-props
  ;
  ; @return (map)
  ; {:border-color (keyword or string)
  ;  :border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :font-weight (keyword or integer)
  ;  :gap (keyword, px or string)
  ;  :icon-position (keyword)
  ;  :icon-size (keyword)
  ;  :indent (map)
  ;  :outdent (map)
  ;  :text-transform (keyword)
  ;  :width (keyword, px or string)}
  [title-props]
  (merge {:border-color     :default
          :border-position  :bottom
          :border-width     :xs
          :font-weight      :semi-bold
          :gap              :auto
          :icon-position    :right
          :icon-size        :xl
          :indent           {:bottom :xxs}
          :outdent          {:bottom :s}
          :text-transform   :uppercase
          :width            :auto}
         (-> title-props)))
