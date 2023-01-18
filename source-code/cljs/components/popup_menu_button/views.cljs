
(ns components.popup-menu-button.views
    (:require [components.popup-menu-button.presets    :as popup-menu-button.presets]
              [components.popup-menu-button.prototypes :as popup-menu-button.prototypes]
              [elements.api                            :as elements]
              [pretty-css.api                          :as pretty-css]
              [random.api                              :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; XXX#0714 (source-code/cljs/elements/button/views.cljs)
  ; The popup-menu-button component is based on the button element.
  ; For more information check out the documentation of the button element.
  ;
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; {:border-radius (map)(opt)
  ;   Default: {:all :s}
  ;  :font-size (keyword)(opt)
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
  ;   Default: {:horizontal :xxs :vertical :xxs}
  ;  :outdent (map)(opt)
  ;   Default: {:vertical :xs}
  ;  :preset (keyword)(opt)
  ;   :highlight}
  ;
  ; @usage
  ; [popup-menu-button {...}]
  ;
  ; @usage
  ; [popup-menu-button :my-popup-menu-button {...}]
  ([button-props]
   [component (random/generate-keyword) button-props])

  ([button-id button-props]
   (let [button-props (pretty-css/apply-preset popup-menu-button.presets/BUTTON-PROPS-PRESETS button-props)
         button-props (popup-menu-button.prototypes/button-props-prototype button-props)]
        [elements/button button-id button-props])))
