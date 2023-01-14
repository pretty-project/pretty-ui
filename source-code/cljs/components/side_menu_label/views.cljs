
(ns components.side-menu-label.views
    (:require [components.side-menu-label.prototypes :as side-menu-label.prototypes]
              [elements.api                          :as elements]
              [random.api                            :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; XXX#0439 (source-code/cljs/elements/label/views.cljs)
  ; The side-menu-label component is based on the label element.
  ; Check out the documentation of the label element for more information.
  ;
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :disabled? (boolean)(opt)
  ;  :icon (keyword)
  ;  :icon-family (keyword)(opt)
  ;   Default: :material-icons-filled
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [side-menu-label {...}]
  ;
  ; @usage
  ; [side-menu-label :my-side-menu-label {...}]
  ([label-props]
   [component (random/generate-keyword) label-props])

  ([label-id label-props]
   (let [label-props (side-menu-label.prototypes/label-props-prototype label-props)]
        [elements/label label-id label-props])))
