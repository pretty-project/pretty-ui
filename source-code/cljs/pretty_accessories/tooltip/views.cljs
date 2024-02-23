
(ns pretty-accessories.tooltip.views
    (:require [fruits.random.api :as random]
              [pretty-accessories.tooltip.attributes :as tooltip.attributes]
              [pretty-accessories.tooltip.prototypes :as tooltip.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- tooltip
  ; @ignore
  ;
  ; @param (keyword) tooltip-id
  ; @param (map) tooltip-props
  ; {:icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  ...}
  [tooltip-id {:keys [icon label] :as tooltip-props}]
  [:div (tooltip.attributes/tooltip-attributes tooltip-id tooltip-props)
        [:div (tooltip.attributes/tooltip-body-attributes tooltip-id tooltip-props)
              (cond label [:div (tooltip.attributes/tooltip-label-attributes tooltip-id tooltip-props) label]
                    icon  [:i   (tooltip.attributes/tooltip-icon-attributes  tooltip-id tooltip-props) icon])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) tooltip-id
  ; @param (map) tooltip-props
  [tooltip-id tooltip-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    tooltip-id tooltip-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount tooltip-id tooltip-props))
                         :reagent-render         (fn [_ tooltip-props] [tooltip tooltip-id tooltip-props])}))

(defn view
  ; @description
  ; Tooltip accessory for elements.
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Icon properties](pretty-core/cljs/pretty-properties/api.html#icon-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Label properties](pretty-core/cljs/pretty-properties/api.html#label-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Position properties](pretty-core/cljs/pretty-properties/api.html#position-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) tooltip-id
  ; @param (map) tooltip-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-accessories/tooltip.png)
  ; [tooltip {:border-radius {:all :s}
  ;           :label         "My tooltip"
  ;           :fill-color    :invert
  ;           :indent        {:all :xxs}
  ;           :position      :br
  ;           :text-color    :invert}]
  ([tooltip-props]
   [view (random/generate-keyword) tooltip-props])

  ([tooltip-id tooltip-props]
   ; @note (tutorials#parameterizing)
   (fn [_ tooltip-props]
       (let [tooltip-props (pretty-presets.engine/apply-preset     tooltip-id tooltip-props)
             tooltip-props (tooltip.prototypes/tooltip-props-prototype tooltip-id tooltip-props)]
            [view-lifecycles tooltip-id tooltip-props]))))
