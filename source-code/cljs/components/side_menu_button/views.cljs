
(ns components.side-menu-button.views
    (:require [components.side-menu-button.presets    :as side-menu-button.presets]
              [components.side-menu-button.prototypes :as side-menu-button.prototypes]
              [elements.api                           :as elements]
              [pretty-css.api                         :as pretty-css]
              [random.api                             :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; XXX#0714 (source-code/cljs/elements/button/views.cljs)
  ; The side-menu-button component is based on the button element.
  ; For more information check out the documentation of the button element.
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
  ;   Default: {:horizontal :xs :left :s :right :xl}
  ;  :preset (keyword)(opt)}
  ;
  ; @usage
  ; [side-menu-button {...}]
  ;
  ; @usage
  ; [side-menu-button :my-side-menu-button {...}]
  ([button-props]
   [component (random/generate-keyword) button-props])

  ([button-id button-props]
   (let [button-props (pretty-css/apply-preset side-menu-button.presets/BUTTON-PROPS-PRESETS button-props)
         button-props (side-menu-button.prototypes/button-props-prototype button-props)]
        [elements/button button-id button-props])))
