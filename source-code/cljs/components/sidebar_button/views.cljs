
(ns components.sidebar-button.views
    (:require [components.sidebar-button.prototypes :as sidebar-button.prototypes]
              [elements.api                         :as elements]
              [random.api                           :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; XXX#0714 (source-code/cljs/elements/button/views.cljs)
  ; The sidebar-button component is based on the button element.
  ; Check out the documentation of the button element for more information.
  ;
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;   Default: :invert
  ;  :icon (keyword)(opt)
  ;  :icon-color (string or keyword)(opt)
  ;  :icon-family (keyword)(opt)
  ;  :label (metamorphic-content)
  ;  :on-click (metamorphic-event)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [sidebar-button {...}]
  ;
  ; @usage
  ; [sidebar-button :my-sidebar-button {...}]
  ([button-props]
   [component (random/generate-keyword) button-props])

  ([button-id button-props]
   (let [button-props (sidebar-button.prototypes/button-props-prototype button-props)]
        [elements/button button-id button-props])))
