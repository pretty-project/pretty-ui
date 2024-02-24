
(ns pretty-accessories.badge.views
    (:require [fruits.random.api :as random]
              [pretty-accessories.badge.attributes :as badge.attributes]
              [pretty-accessories.badge.prototypes :as badge.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- badge
  ; @ignore
  ;
  ; @param (keyword) badge-id
  ; @param (map) badge-props
  ; {:icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  ...}
  [badge-id {:keys [icon label] :as badge-props}]
  [:div (badge.attributes/badge-attributes badge-id badge-props)
        [:div (badge.attributes/badge-inner-attributes badge-id badge-props)
              (cond label [:div (badge.attributes/badge-label-attributes badge-id badge-props) label]
                    icon  [:i   (badge.attributes/badge-icon-attributes  badge-id badge-props) icon])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) badge-id
  ; @param (map) badge-props
  [badge-id badge-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    badge-id badge-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount badge-id badge-props))
                         :reagent-render         (fn [_ badge-props] [badge badge-id badge-props])}))

(defn view
  ; @description
  ; Badge accessory for elements.
  ;
  ; @links Implemented properties
  ; [Animation properties](pretty-core/cljs/pretty-properties/api.html#animation-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Icon properties](pretty-core/cljs/pretty-properties/api.html#icon-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Label properties](pretty-core/cljs/pretty-properties/api.html#label-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) badge-id
  ; @param (map) badge-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-accessories/badge.png)
  ; [badge {:border-radius {:all :s}
  ;         :label         "My badge"
  ;         :fill-color    :highlight
  ;         :indent        {:all :xxs}
  ;         :position      :br}]
  ([badge-props]
   [view (random/generate-keyword) badge-props])

  ([badge-id badge-props]
   ; @note (tutorials#parameterizing)
   (fn [_ badge-props]
       (let [badge-props (pretty-presets.engine/apply-preset     badge-id badge-props)
             badge-props (badge.prototypes/badge-props-prototype badge-id badge-props)]
            [view-lifecycles badge-id badge-props]))))
