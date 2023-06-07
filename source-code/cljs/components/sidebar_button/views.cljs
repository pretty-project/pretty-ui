
(ns components.sidebar-button.views
    (:require [components.sidebar-button.prototypes :as sidebar-button.prototypes]
              [elements.api                         :as elements]
              [random.api                           :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; XXX#0714 (source-code/cljs/elements/button/views.cljs)
  ; The 'sidebar-button' component is based on the 'button' element.
  ; For more information check out the documentation of the 'button' element.
  ;
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; {:color (keyword or string)(opt)
  ;   Default: :invert
  ;  :font-size (keyword)(opt)
  ;   Default: :xs
  ;  :font-weight (keyword)(opt)
  ;   Default: :medium
  ;  :gap (keyword)(opt)
  ;   Default: :xs
  ;  :hover-color (keyword or string)(opt)
  ;   Default: :invert
  ;  :icon-size (keyword)(opt)
  ;   Default: :m
  ;  :horizontal-align (keyword)(opt)
  ;   Default: :left
  ;  :hover-color (keyword)(opt)
  ;   Default: invert}
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
