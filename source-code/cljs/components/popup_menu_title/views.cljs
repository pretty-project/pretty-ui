
(ns components.popup-menu-title.views
    (:require [components.popup-menu-title.prototypes :as popup-menu-title.prototypes]
              [pretty-elements.api                           :as pretty-elements]
              [random.api                             :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @info
  ; XXX#0439 (source-code/cljs/pretty_elements/label/views.cljs)
  ; The 'popup-menu-title' component is based on the 'label' element.
  ; For more information check out the documentation of the 'label' element.
  ;
  ; @param (keyword)(opt) title-id
  ; @param (map) title-props
  ; {:border-color (keyword)
  ;   Default: :default
  ;  :border-position (keyword)
  ;   Default: :bottom
  ;  :border-width (keyword)
  ;   Default: :xs
  ;  :font-weight (keyword)
  ;   Default: :semi-bold
  ;  :gap (keyword)
  ;   Default: :auto
  ;  :icon-position (keyword)
  ;   Default: :right
  ;  :icon-size (keyword)
  ;   Default: :xl
  ;  :indent (map)
  ;   Default: {:bottom :xxs}
  ;  :outdent (map)
  ;   Default: {:bottom :s}
  ;  :text-transform (keyword)(opt)
  ;   Default: :uppercase
  ;  :width (keyword)(opt)
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
   (fn [_ title-props] ; XXX#0106 (README.md#parametering)
       (let [title-props (popup-menu-title.prototypes/title-props-prototype title-props)]
            [pretty-elements/label title-id title-props]))))
