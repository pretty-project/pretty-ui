
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
  ;  :label (metamorphic-content)(opt)}
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
  ; @param (keyword)(opt) tooltip-id
  ; @param (map) tooltip-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-color (keyword or string)(opt)
  ;  :icon-family (keyword)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :label (metamorphic-content)(opt)
  ;  :label-placeholder (metamorphic-content)(opt)
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :position (keyword)(opt)
  ;  :position-base (keyword)(opt)
  ;  :position-method (keyword)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :text-color (keyword or string)(opt)
  ;  :text-overflow (keyword)(opt)
  ;  :text-transform (keyword)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [tooltip {...}]
  ;
  ; @usage
  ; [tooltip :my-tooltip {...}]
  ([tooltip-props]
   [view (random/generate-keyword) tooltip-props])

  ([tooltip-id tooltip-props]
   ; @note (tutorials#parameterizing)
   (fn [_ tooltip-props]
       (let [tooltip-props (pretty-presets.engine/apply-preset     tooltip-id tooltip-props)
             tooltip-props (tooltip.prototypes/tooltip-props-prototype tooltip-id tooltip-props)]
            [view-lifecycles tooltip-id tooltip-props]))))
