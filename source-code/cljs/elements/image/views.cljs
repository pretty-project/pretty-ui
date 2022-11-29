
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.image.views
    (:require [elements.image.helpers    :as image.helpers]
              [elements.image.prototypes :as image.prototypes]
              [random.api                :as random]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- image
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  [image-id image-props]
  [:div.e.image (image.helpers/image-attributes image-id image-props)
                [:img.e-image--body (image.helpers/image-body-attributes image-id image-props)]])

(defn element
  ; @param (keyword)(opt) image-id
  ; @param (map) image-props
  ;  {:alt (string)(opt)
  ;   :class (keyword or keywords in vector)(opt)
  ;   :error-src (string)(opt)
  ;    TODO
  ;   :height (string)(opt)
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :lazy-loading? (boolean)(opt)
  ;    Default: false
  ;    TODO
  ;   :src (string)(opt)
  ;   :style (map)(opt)
  ;   :width (string)(opt)}
  ;
  ; @usage
  ;  [image {...}]
  ;
  ; @usage
  ;  [image :my-image {...}]
  ([image-props]
   [element (random/generate-keyword) image-props])

  ([image-id image-props]
   (let [];image-props (image.prototypes/image-props-prototype image-id image-props)
        [image image-id image-props])))
