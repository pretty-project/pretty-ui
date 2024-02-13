
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
  ;  :label (metamorphic-content)(opt)}
  [cover-id {:keys [icon label] :as cover-props}]
  [:div (cover.attributes/cover-attributes cover-id cover-props)
        [:div (cover.attributes/cover-body-attributes cover-id cover-props)
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
  ; @param (keyword)(opt) cover-id
  ; @param (map) cover-props
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
  ;  :opacity (keyword or number)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :text-color (keyword or string)(opt)
  ;  :text-overflow (keyword)(opt)
  ;  :text-transform (keyword)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [cover {...}]
  ;
  ; @usage
  ; [cover :my-cover {...}]
  ([cover-props]
   [view (random/generate-keyword) cover-props])

  ([cover-id cover-props]
   ; @note (tutorials#parameterizing)
   (fn [_ cover-props]
       (let [cover-props (pretty-presets.engine/apply-preset     cover-id cover-props)
             cover-props (cover.prototypes/cover-props-prototype cover-id cover-props)]
            [view-lifecycles cover-id cover-props]))))
