
(ns pretty-accessories.cover.views
    (:require [fruits.random.api :as random]
              [pretty-accessories.cover.attributes :as cover.attributes]
              [pretty-accessories.cover.prototypes :as cover.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cover
  ; @ignore
  ;
  ; @param (keyword) cover-id
  ; @param (map) cover-props
  ; {:icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  ...}
  [cover-id {:keys [icon label] :as cover-props}]
  [:div (cover.attributes/cover-attributes cover-id cover-props)
        [:div (cover.attributes/cover-inner-attributes cover-id cover-props)
              (cond label [:div (cover.attributes/cover-label-attributes cover-id cover-props) label]
                    icon  [:i   (cover.attributes/cover-icon-attributes  cover-id cover-props) icon])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) cover-id
  ; @param (map) cover-props
  [cover-id cover-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    cover-id cover-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount cover-id cover-props))
                         :reagent-render         (fn [_ cover-props] [cover cover-id cover-props])}))

(defn view
  ; @description
  ; Cover accessory for elements.
  ;
  ; @links Implemented properties
  ; [Animation properties](pretty-core/cljs/pretty-properties/api.html#animation-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Icon properties](pretty-core/cljs/pretty-properties/api.html#icon-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Label properties](pretty-core/cljs/pretty-properties/api.html#label-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Overlay properties](pretty-core/cljs/pretty-properties/api.html#overlay-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ; [Visibility properties](pretty-core/cljs/pretty-properties/api.html#visibility-properties)
  ;
  ; @param (keyword)(opt) cover-id
  ; @param (map) cover-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-accessories/cover.png)
  ; [cover {:fill-color :highlight
  ;         :label      "My cover"
  ;         :opacity    :medium}]
  ([cover-props]
   [view (random/generate-keyword) cover-props])

  ([cover-id cover-props]
   ; @note (tutorials#parameterizing)
   (fn [_ cover-props]
       (let [cover-props (pretty-presets.engine/apply-preset     cover-id cover-props)
             cover-props (cover.prototypes/cover-props-prototype cover-id cover-props)]
            [view-lifecycles cover-id cover-props]))))
