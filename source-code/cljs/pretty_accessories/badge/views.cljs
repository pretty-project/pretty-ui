
(ns pretty-accessories.badge.views
    (:require [fruits.random.api :as random]
              [pretty-accessories.badge.attributes :as badge.attributes]
              [pretty-accessories.badge.prototypes :as badge.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- badge
  ; @ignore
  ;
  ; @param (keyword) badge-id
  ; @param (map) badge-props
  ; {:icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)}
  [badge-id {:keys [icon label] :as badge-props}]
  [:div (badge.attributes/badge-attributes badge-id badge-props)
        [:div (badge.attributes/badge-body-attributes badge-id badge-props)
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
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    badge-id badge-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount badge-id badge-props))
                       :reagent-render         (fn [_ badge-props] [badge badge-id badge-props])}))

(defn view
  ; @param (keyword)(opt) badge-id
  ; @param (map) badge-props
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
  ; [badge {...}]
  ;
  ; @usage
  ; [badge :my-badge {...}]
  ([badge-props]
   [view (random/generate-keyword) badge-props])

  ([badge-id badge-props]
   ; @note (tutorials#parameterizing)
   (fn [_ badge-props]
       (let [badge-props (pretty-presets.engine/apply-preset     badge-id badge-props)
             badge-props (badge.prototypes/badge-props-prototype badge-id badge-props)]
            [view-lifecycles badge-id badge-props]))))
