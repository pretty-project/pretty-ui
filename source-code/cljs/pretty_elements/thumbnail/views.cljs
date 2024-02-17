
(ns pretty-elements.thumbnail.views
    (:require [fruits.random.api                    :as random]
              [pretty-elements.engine.api           :as pretty-elements.engine]
              [pretty-elements.thumbnail.attributes :as thumbnail.attributes]
              [pretty-elements.thumbnail.prototypes :as thumbnail.prototypes]
              [pretty-presets.engine.api            :as pretty-presets.engine]
              [pretty-accessories.api :as pretty-accessories]
              [dynamic-props.api :as dynamic-props]
              [reagent.core :as reagent]
              [pretty-accessories.api :as pretty-accessories]
              [lazy-loader.api :as lazy-loader]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- thumbnail-load-sensor
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  [thumbnail-id thumbnail-props]
  (let [sensor-props (thumbnail.prototypes/sensor-props-prototype thumbnail-id thumbnail-props)]
       [lazy-loader/image-sensor thumbnail-id sensor-props]))

(defn- thumbnail
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:badge (map)(opt)
  ;  :cover (map)(opt)
  ;  :icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :loaded? (boolean)(opt)
  ;  :marker (map)(opt)
  ;  ...}
  [thumbnail-id {:keys [badge cover icon label loaded? marker] :as thumbnail-props}]
  [:div (thumbnail.attributes/thumbnail-attributes thumbnail-id thumbnail-props)
        [thumbnail-load-sensor                     thumbnail-id thumbnail-props]
        [(pretty-elements.engine/clickable-auto-tag      thumbnail-id thumbnail-props)
         (thumbnail.attributes/thumbnail-body-attributes thumbnail-id thumbnail-props)
         (if loaded? [:div (thumbnail.attributes/thumbnail-canvas-attributes thumbnail-id thumbnail-props)]
                     [:i   (thumbnail.attributes/thumbnail-icon-attributes   thumbnail-id thumbnail-props) icon])
         (if label   [:div (thumbnail.attributes/thumbnail-label-attributes  thumbnail-id thumbnail-props) label])
         (if badge   [pretty-accessories/badge  thumbnail-id badge])
         (if marker  [pretty-accessories/marker thumbnail-id marker])
         (if cover   [pretty-accessories/cover  thumbnail-id cover])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  [thumbnail-id thumbnail-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    thumbnail-id thumbnail-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount thumbnail-id thumbnail-props))
                         :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   thumbnail-id thumbnail-props %))
                         :reagent-render         (fn [_ thumbnail-props] [thumbnail thumbnail-id thumbnail-props])}))

(defn view
  ; @description
  ; Optionally clickable thumbnail element with built-in lazy loader and animated loading icon.
  ;
  ; @links Implemented accessories
  ; [Badge](pretty-ui/cljs/pretty-accessories/api.html#badge)
  ; [Cover](pretty-ui/cljs/pretty-accessories/api.html#cover)
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
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Icon properties](pretty-core/cljs/pretty-properties/api.html#icon-properties)
  ; [Keypress properties](pretty-core/cljs/pretty-properties/api.html#keypress-properties)
  ; [Label properties](pretty-core/cljs/pretty-properties/api.html#label-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) thumbnail-id
  ; @param (map) thumbnail-props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (thumbnail.png)
  ; [thumbnail {:background-uri  "/my-thumbnail.png"
  ;             :badge           {:icon     :fullscreen
  ;                               :position :tr}
  ;             :border-radius   {:all :s}
  ;             :background-size :cover
  ;             :label           "My thumbnail"
  ;             :height          :l
  ;             :width           :l}]
  ([thumbnail-props]
   [view (random/generate-keyword) thumbnail-props])

  ([thumbnail-id thumbnail-props]
   ; @note (tutorials#parameterizing)
   (fn [_ thumbnail-props]
       (let [thumbnail-props (pretty-presets.engine/apply-preset             thumbnail-id thumbnail-props)
             thumbnail-props (thumbnail.prototypes/thumbnail-props-prototype thumbnail-id thumbnail-props)
             thumbnail-props (pretty-elements.engine/element-timeout-props   thumbnail-id thumbnail-props)
             thumbnail-props (dynamic-props/import-props                     thumbnail-id thumbnail-props)]
            [view-lifecycles thumbnail-id thumbnail-props]))))
