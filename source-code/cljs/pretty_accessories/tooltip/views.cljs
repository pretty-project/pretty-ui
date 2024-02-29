
(ns pretty-accessories.tooltip.views
    (:require [fruits.random.api :as random]
              [pretty-accessories.tooltip.attributes :as tooltip.attributes]
              [pretty-accessories.tooltip.prototypes :as tooltip.prototypes]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [pretty-accessories.icon.views :as icon.views]
              [pretty-accessories.label.views :as label.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- tooltip
  ; @ignore
  ;
  ; @param (keyword) tooltip-id
  ; @param (map) tooltip-props
  ; {:icon (map)(opt)
  ;  :label (map)(opt)
  ;  ...}
  [tooltip-id {:keys [icon label] :as tooltip-props}]
  [:div (tooltip.attributes/tooltip-attributes tooltip-id tooltip-props)
        [:div (tooltip.attributes/tooltip-inner-attributes tooltip-id tooltip-props)
              (cond label [label.views/view tooltip-id label]
                    icon  [icon.views/view  tooltip-id icon])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @description
  ; Tooltip accessory for elements.
  ;
  ; @links Implemented accessories
  ; [Icon](pretty-core/cljs/pretty-accessories/api.html#icon)
  ; [Label](pretty-core/cljs/pretty-accessories/api.html#label)
  ;
  ; @links Implemented properties
  ; [Animation properties](pretty-core/cljs/pretty-properties/api.html#animation-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) tooltip-id
  ; @param (map) tooltip-props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-accessories/tooltip.png)
  ; [tooltip {:border-radius {:all :s}
  ;           :label         {:content "My tooltip" :text-color :invert}
  ;           :fill-color    :invert
  ;           :indent        {:all :xxs}
  ;           :position      :br}]
  ([tooltip-props]
   [view (random/generate-keyword) tooltip-props])

  ([tooltip-id tooltip-props]
   ; @note (tutorials#parameterizing)
   (fn [_ tooltip-props]
       (let [tooltip-props (pretty-presets.engine/apply-preset         tooltip-id tooltip-props)
             tooltip-props (tooltip.prototypes/tooltip-props-prototype tooltip-id tooltip-props)]
            [tooltip tooltip-id tooltip-props]))))
