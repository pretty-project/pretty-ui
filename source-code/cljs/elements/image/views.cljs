
(ns elements.image.views
    (:require [elements.image.attributes :as image.attributes]
              [elements.image.prototypes :as image.prototypes]
              [random.api                :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- image
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  [image-id image-props]
  [:div (image.attributes/image-attributes image-id image-props)
        [:img (image.attributes/image-body-attributes image-id image-props)]])

(defn element
  ; @param (keyword)(opt) image-id
  ; @param (map) image-props
  ; {:alt (string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :error-src (string)(opt)
  ;   TODO
  ;  :height (string)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :lazy-load? (boolean)(opt)
  ;   Default: false
  ;   TODO
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :src (string)(opt)
  ;  :style (map)(opt)
  ;  :width (string)(opt)}
  ;
  ; @usage
  ; [image {...}]
  ;
  ; @usage
  ; [image :my-image {...}]
  ([image-props]
   [element (random/generate-keyword) image-props])

  ([image-id image-props]
   (let [] ; image-props (image.prototypes/image-props-prototype image-id image-props)
        [image image-id image-props])))
