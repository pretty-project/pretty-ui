
(ns components.side-menu-label.views
    (:require [components.side-menu-label.prototypes :as side-menu-label.prototypes]
              [elements.api                          :as elements]
              [random.api                            :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :font-size (keyword)(opt)
  ;   Default: :xs
  ;  :font-weight (keyword)(opt)
  ;  :icon (keyword)
  ;  :icon-color (string or keyword)
  ;  :icon-family (keyword)(opt)
  ;   Default: :material-icons-filled
  ;   Default: {:left :s :horizontal :xs}
  ;  :label (metamorphic-content)
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
