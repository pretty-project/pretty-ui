
(ns components.side-menu-label.views
    (:require [components.side-menu-label.prototypes :as side-menu-label.prototypes]
              [fruits.random.api                     :as random]
              [pretty-elements.api                   :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @info
  ; XXX#0439 (source-code/cljs/pretty_elements/label/views.cljs)
  ; The 'side-menu-label' component is based on the 'label' element.
  ; For more information, check out the documentation of the 'label' element.
  ;
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; {:font-size (keyword, px or string)(opt)
  ;   Default: :xs
  ;  :gap (keyword, px or string)(opt)
  ;   Default: :xs
  ;  :horizontal-align (keyword)(opt)
  ;   Default: :left
  ;  :icon-size (keyword)(opt)
  ;   Default: :m
  ;  :indent (map)(opt)
  ;   Default: {:horizontal :xs :vertical :s}
  ;  :style (map)(opt)
  ;   Default: {:max-width "var(--element-size-m)"}}
  ;
  ; @usage
  ; [side-menu-label {...}]
  ;
  ; @usage
  ; [side-menu-label :my-side-menu-label {...}]
  ([label-props]
   [component (random/generate-keyword) label-props])

  ([label-id label-props]
   ; @note (tutorials#parametering)
   (fn [_ label-props]
       (let [label-props (side-menu-label.prototypes/label-props-prototype label-props)]
            [pretty-elements/label label-id label-props]))))
