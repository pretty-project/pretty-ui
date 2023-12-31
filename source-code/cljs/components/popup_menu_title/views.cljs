
(ns components.popup-menu-title.views
    (:require [components.popup-menu-title.prototypes :as popup-menu-title.prototypes]
              [fruits.random.api                      :as random]
              [pretty-elements.api                    :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @info
  ; XXX#0439 (source-code/cljs/pretty_elements/label/views.cljs)
  ; The 'popup-menu-title' component is based on the 'label' element.
  ; For more information, check out the documentation of the 'label' element.
  ;
  ; @param (keyword)(opt) title-id
  ; @param (map) title-props
  ; {:border-color (keyword or string)
  ;   Default: :default
  ;  :border-position (keyword)
  ;   Default: :bottom
  ;  :border-width (keyword, px or string)
  ;   Default: :xs
  ;  :font-weight (keyword or integer)
  ;   Default: :semi-bold
  ;  :gap (keyword, px or string)
  ;   Default: :auto
  ;  :icon-position (keyword)
  ;   Default: :right
  ;  :icon-size (keyword, px or string)
  ;   Default: :xl
  ;  :indent (map)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;   Default: {:bottom :xxs}
  ;  :outdent (map)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;   Default: {:bottom :s}
  ;  :text-transform (keyword)(opt)
  ;   Default: :uppercase
  ;  :width (keyword, px or string)(opt)
  ;   Default: :auto}
  ;
  ; @usage
  ; [popup-menu-title {...}]
  ;
  ; @usage
  ; [popup-menu-title :my-popup-menu-title {...}]
  ([title-props]
   [component (random/generate-keyword) title-props])

  ([title-id title-props]
   ; @note (tutorials#parametering)
   (fn [_ title-props]
       (let [title-props (popup-menu-title.prototypes/title-props-prototype title-props)]
            [pretty-elements/label title-id title-props]))))
