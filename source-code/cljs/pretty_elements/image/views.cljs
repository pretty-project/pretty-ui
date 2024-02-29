
(ns pretty-elements.image.views
    (:require [fruits.random.api                    :as random]
              [pretty-elements.engine.api           :as pretty-elements.engine]
              [pretty-elements.image.attributes :as image.attributes]
              [pretty-elements.image.prototypes :as image.prototypes]
              [pretty-presets.engine.api            :as pretty-presets.engine]
              [pretty-accessories.api :as pretty-accessories]
              [dynamic-props.api :as dynamic-props]
              [reagent.core :as reagent]
              [pretty-models.api             :as pretty-models]
              [pretty-accessories.api :as pretty-accessories]
              [lazy-loader.api :as lazy-loader]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn broken-image
  ; @ignore
  []
  [:svg {:xmlns "http://www.w3.org/2000/svg" :height "24px" :width "24p0" :view-box "0 0 24 24" :fill "#000"}
        [:path {:fill "none" :d "M0 0h24v24H0z"}]
        [:path {:fill "none" :d "M0 0h24v24H0zm0 0h24v24H0zm21 19c0 1.1-.9 2-2 2H5c-1.1 0-2-.9-2-2V5c0-1.1.9-2 2-2h14c1.1 0 2 .9 2 2"}]
        [:path {             :d "M21 5v6.59l-3-3.01-4 4.01-4-4-4 4-3-3.01V5c0-1.1.9-2 2-2h14c1.1 0 2 .9 2 2zm-3 6.42l3 3.01V19c0 1.1-.9 2-2 2H5c-1.1 0-2-.9-2-2v-6.58l3 2.99 4-4 4 4 4-3.99z"}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- image
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ; {:badge (map)(opt)
  ;  :cover (map)(opt)
  ;  :icon (map)(opt)
  ;  :label (map)(opt)
  ;  :loaded? (boolean)(opt)
  ;  :marker (map)(opt)
  ;  :sensor (map)(opt)
  ;  ...}
  [image-id {:keys [badge cover icon label loaded? marker sensor] :as image-props}]
  [:div (image.attributes/image-attributes image-id image-props)
        [(pretty-models/clickable-auto-tag        image-id image-props)
         (image.attributes/image-inner-attributes image-id image-props)
         (if     loaded? [:div (image.attributes/image-canvas-attributes image-id image-props)])
         (if-not loaded? [lazy-loader/image-sensor  image-id sensor])
         (if-not loaded? [pretty-accessories/icon   image-id icon])
         (if     label   [pretty-accessories/label  image-id label])
         (if     badge   [pretty-accessories/badge  image-id badge])
         (if     marker  [pretty-accessories/marker image-id marker])
         (if     cover   [pretty-accessories/cover  image-id cover])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  [image-id image-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    image-id image-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount image-id image-props))
                         :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   image-id image-props %))
                         :reagent-render         (fn [_ image-props] [image image-id image-props])}))

(defn view
  ; @description
  ; Optionally clickable image element with built-in lazy loader and animated loading icon.
  ;
  ; @links Implemented accessories
  ; [Badge](pretty-ui/cljs/pretty-accessories/api.html#badge)
  ; [Cover](pretty-ui/cljs/pretty-accessories/api.html#cover)
  ; [Icon](pretty-ui/cljs/pretty-accessories/api.html#icon)
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ; [Marker](pretty-ui/cljs/pretty-accessories/api.html#marker)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented properties
  ; [Anchor properties](pretty-core/cljs/pretty-properties/api.html#anchor-properties)
  ; [Animation properties](pretty-core/cljs/pretty-properties/api.html#animation-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Background image properties](pretty-core/cljs/pretty-properties/api.html#background-image-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Clickable state properties](pretty-core/cljs/pretty-properties/api.html#clickable-state-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Effect properties](pretty-core/cljs/pretty-properties/api.html#effect-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Keypress properties](pretty-core/cljs/pretty-properties/api.html#keypress-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) image-id
  ; @param (map) image-props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/image.png)
  ; [image {:background-size :cover
  ;         :background-uri  "/my-image.png"
  ;         :badge           {:icon {:icon-name :fullscreen} :position :tr}
  ;         :border-radius   {:all :s}
  ;         :label           {:content "My image"}
  ;         :outer-height    :s
  ;         :outer-width     :l}]
  ([image-props]
   [view (random/generate-keyword) image-props])

  ([image-id image-props]
   ; @note (tutorials#parameterizing)
   (fn [_ image-props]
       (let [image-props (pretty-presets.engine/apply-preset           image-id image-props)
             image-props (image.prototypes/image-props-prototype       image-id image-props)
             image-props (pretty-elements.engine/element-timeout-props image-id image-props)
             image-props (dynamic-props/import-props                   image-id image-props)]
            [view-lifecycles image-id image-props]))))
