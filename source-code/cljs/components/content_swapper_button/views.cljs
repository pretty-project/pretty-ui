
(ns components.content-swapper-button.views
    (:require [components.content-swapper-button.prototypes :as content-swapper-button.prototypes]
              [fruits.random.api                            :as random]
              [pretty-elements.api                          :as pretty-elements]
              [pretty-elements.api                          :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @info
  ; XXX#0714 (source-code/cljs/pretty_elements/button/views.cljs)
  ; The 'content-swapper-button' component is based on the 'button' element.
  ; For more information, check out the documentation of the 'button' element.
  ;
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; {:font-size (keyword, px or string)(opt)
  ;   Default: :xs
  ;  :gap (keyword, px or string)(opt)
  ;   Default: :auto
  ;  :horizontal-align (keyword)(opt)
  ;   Default: :left
  ;  :hover-color (keyword or string)(opt)
  ;   Default: :highlight
  ;  :icon (keyword)(opt)
  ;   Default: :chevron_right
  ;  :icon-position (keyword)(opt)
  ;   Default: :right
  ;  :icon-size (keyword)(opt)
  ;   Default: :m
  ;  :indent (map)(opt)
  ;   Default: {:all :xs}
  ;  :width (keyword, px or string)(opt)
  ;   Default: :auto}
  ;
  ; @usage
  ; [content-swapper-button {...}]
  ;
  ; @usage
  ; [content-swapper-button :my-content-swapper-button {...}]
  ([button-props]
   [component (random/generate-keyword) button-props])

  ([button-id button-props]
   ; @note (tutorials#parametering)
   (fn [_ button-props]
       (let [button-props (content-swapper-button.prototypes/button-props-prototype button-props)]
            [pretty-elements/button button-id button-props]))))
