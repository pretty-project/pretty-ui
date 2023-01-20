
(ns components.popup-menu-label.views
    (:require [components.popup-menu-label.prototypes :as popup-menu-label.prototypes]
              [elements.api                           :as elements]
              [random.api                             :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; XXX#0439 (source-code/cljs/elements/label/views.cljs)
  ; The popup-menu-label component is based on the label element.
  ; For more information check out the documentation of the label element.
  ;
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; {:color (keyword or string)(opt)
  ;   Default: :muted
  ;  :gap (keyword)(opt)
  ;   Default: :xs
  ;  :horizontal-align (keyword)(opt)
  ;   Default: :left
  ;  :icon-size (keyword)(opt)
  ;   Default: :m
  ;  :indent (map)(opt)
  ;   Default: {:horizontal :xxs :vertical :s}}
  ;
  ; @usage
  ; [popup-menu-label {...}]
  ;
  ; @usage
  ; [popup-menu-label :my-popup-menu-label {...}]
  ([label-props]
   [component (random/generate-keyword) label-props])

  ([label-id label-props]
   (let [label-props (popup-menu-label.prototypes/label-props-prototype label-props)]
        [elements/label label-id label-props])))
