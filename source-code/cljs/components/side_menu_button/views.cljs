
(ns components.side-menu-button.views
    (:require [components.side-menu-button.presets    :as side-menu-button.presets]
              [components.side-menu-button.prototypes :as side-menu-button.prototypes]
              [elements.api                           :as elements]
              [random.api                             :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @info
  ; XXX#0714 (source-code/cljs/elements/button/views.cljs)
  ; The 'side-menu-button' component is based on the 'button' element.
  ; For more information check out the documentation of the 'button' element.
  ;
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; {:font-size (keyword)(opt)
  ;   Default: :xs
  ;  :gap (keyword)(opt)
  ;   Default: :xs
  ;  :horizontal-align (keyword)(opt)
  ;   Default: :left
  ;  :hover-color (keyword or string)(opt)
  ;   Default: :highlight
  ;  :icon-size (keyword)(opt)
  ;   Default: :m
  ;  :indent (map)(opt)
  ;   Default: {:horizontal :xs :left :s :right :xl}}
  ;
  ; @usage
  ; [side-menu-button {...}]
  ;
  ; @usage
  ; [side-menu-button :my-side-menu-button {...}]
  ([button-props]
   [component (random/generate-keyword) button-props])

  ([button-id button-props]
   (fn [_ button-props] ; XXX#0106 (README.md#parametering)
       (let [button-props (side-menu-button.prototypes/button-props-prototype button-props)]
            [elements/button button-id button-props]))))
