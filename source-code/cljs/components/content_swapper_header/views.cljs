
(ns components.content-swapper-header.views
    (:require [components.content-swapper-header.prototypes :as content-swapper-header.prototypes]
              [fruits.random.api                            :as random]
              [pretty-elements.api                          :as pretty-elements]
              [pretty-elements.api                          :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @info
  ; XXX#0714 (source-code/cljs/pretty_elements/button/views.cljs)
  ; The 'content-swapper-header' component is based on the 'button' element.
  ; For more information, check out the documentation of the 'button' element.
  ;
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ; {:border-color (keyword or string)(opt)
  ;   Default: :highlight
  ;  :border-position (keyword)(opt)
  ;   Default: :bottom
  ;  :font-size (keyword, px or string)(opt)
  ;   Default: :xs
  ;  :gap (keyword, px or string)(opt)
  ;   Default: :auto
  ;  :horizontal-align (keyword)(opt)
  ;   Default: :left
  ;  :hover-color (keyword or string)(opt)
  ;   Default: :highlight
  ;  :icon (keyword)(opt)
  ;   Default: :chevron_left
  ;  :icon-position (keyword)(opt)
  ;   Default: :left
  ;  :icon-size (keyword, px or string)(opt)
  ;   Default: :xl
  ;  :indent (map)(opt)
  ;   Default: {:horizontal :xs}
  ;  :width (keyword, px or string)(opt)
  ;   Default: :auto}
  ;
  ; @usage
  ; [content-swapper-header {...}]
  ;
  ; @usage
  ; [content-swapper-header :my-content-swapper-header {...}]
  ([header-props]
   [component (random/generate-keyword) header-props])

  ([header-id header-props]
   ; @note (tutorials#parametering)
   (fn [_ header-props]
       (let [header-props (content-swapper-header.prototypes/header-props-prototype header-props)]
            [pretty-elements/button header-id header-props]))))
