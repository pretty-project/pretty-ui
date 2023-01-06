
(ns components.side-menu-button.views
    (:require [components.component.helpers           :as component.helpers]
              [components.side-menu-button.presets    :as side-menu-button.presets]
              [components.side-menu-button.prototypes :as side-menu-button.prototypes]
              [elements.api                           :as elements]
              [random.api                             :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; XXX#0714 (source-code/cljs/elements/button/views.cljs)
  ; The side-menu-button component is based on the button element.
  ; Check out the documentation of the button element for more information.
  ;
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :icon (keyword)
  ;  :icon-color (string or keyword)
  ;  :icon-family (keyword)(opt)
  ;   Default: :material-icons-filled
  ;  :label (metamorphic-content)
  ;  :on-click (metamorphic-event)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [side-menu-button {...}]
  ;
  ; @usage
  ; [side-menu-button :my-side-menu-button {...}]
  ([button-props]
   [component (random/generate-keyword) button-props])

  ([button-id button-props]
   (let [button-props (component.helpers/apply-preset side-menu-button.presets/BUTTON-PROPS-PRESETS button-props)
         button-props (side-menu-button.prototypes/button-props-prototype button-props)]
        [elements/button button-id button-props])))
