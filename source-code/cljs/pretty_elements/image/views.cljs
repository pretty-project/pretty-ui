
(ns pretty-elements.image.views
    (:require [fruits.random.api                :as random]
              [pretty-elements.image.attributes :as image.attributes]
              [pretty-elements.image.prototypes :as image.prototypes]
              [pretty-elements.engine.api                :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                      :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn broken-image
  ; @ignore
  []
  [:svg {:xmlns "http://www.w3.org/2000/svg" :height "24px" :width "24p0" :view-box "0 0 24 24" :fill "#000"}
        [:path {:fill "none" :d "M0 0h24v24H0z"}]
        [:path {:fill "none" :d "M0 0h24v24H0zm0 0h24v24H0zm21 19c0 1.1-.9 2-2 2H5c-1.1 0-2-.9-2-2V5c0-1.1.9-2 2-2h14c1.1 0 2 .9 2 2"}]
        [:path {             :d "M21 5v6.59l-3-3.01-4 4.01-4-4-4 4-3-3.01V5c0-1.1.9-2 2-2h14c1.1 0 2 .9 2 2zm-3 6.42l3 3.01V19c0 1.1-.9 2-2 2H5c-1.1 0-2-.9-2-2v-6.58l3 2.99 4-4 4 4 4-3.99z"}]])

(defn- image
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  [image-id image-props]
  [:div (image.attributes/image-attributes image-id image-props)
        [:img (image.attributes/image-body-attributes image-id image-props)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-lifecycles
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  [image-id image-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    image-id image-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount image-id image-props))
                       :reagent-render         (fn [_ image-props] [image image-id image-props])}))

(defn element
  ; @param (keyword)(opt) image-id
  ; @param (map) image-props
  ; {:alt (string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :error-src (string)(opt)
  ;   TODO
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :lazy-load? (boolean)(opt)
  ;   TODO
  ;   Default: false
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :src (string)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; +fill-color, border ...
  ;
  ; @usage
  ; [image {...}]
  ;
  ; @usage
  ; [image :my-image {...}]
  ([image-props]
   [element (random/generate-keyword) image-props])

  ([image-id image-props]
   ; @note (tutorials#parametering)
   (fn [_ image-props]
       (let [image-props (pretty-presets.engine/apply-preset     image-id image-props)
             image-props (image.prototypes/image-props-prototype image-id image-props)]
            [element-lifecycles image-id image-props]))))
