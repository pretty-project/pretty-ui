
(ns pretty-elements.card.views
    (:require [fruits.random.api               :as random]
              [pretty-elements.card.attributes :as card.attributes]
              [pretty-elements.card.prototypes :as card.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- card
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ; {:content (metamorphic-content)(opt)}
  [card-id {:keys [content] :as card-props}]
  [:div (card.attributes/card-attributes card-id card-props)
        [(pretty-elements.engine/clickable-auto-tag card-id card-props)
         (card.attributes/card-body-attributes      card-id card-props)
         (-> content)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  [card-id card-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    card-id card-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount card-id card-props))
                       :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   card-id card-props %))
                       :reagent-render         (fn [_ card-props] [card card-id card-props])}))

(defn view
  ; @param (keyword)(opt) card-id
  ; @param (map) card-props
  ; {:badge-color (keyword or string)(opt)
  ;  :badge-content (metamorphic-content)(opt)
  ;  :badge-position (keyword)(opt)
  ;  :border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :cursor (keyword or string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-pattern (keyword)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :href-target (keyword)(opt)
  ;  :href-uri (string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :keypress (map)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :marker-position (keyword)(opt)
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-click-f (function)(opt)
  ;  :on-click-timeout (ms)(opt)
  ;  :orientation (keyword)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :tab-disabled? (boolean)(opt)
  ;  :theme (keyword)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)
  ;  :tooltip-position (keyword)(opt)
  ;  :vertical-align (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [card {...}]
  ;
  ; @usage
  ; [card :my-card {...}]
  ([card-props]
   [view (random/generate-keyword) card-props])

  ([card-id card-props]
   ; @note (tutorials#parameterizing)
   (fn [_ card-props]
       (let [card-props (pretty-presets.engine/apply-preset           card-id card-props)
             card-props (card.prototypes/card-props-prototype         card-id card-props)
             card-props (pretty-elements.engine/element-timeout-props card-id card-props)]
            [view-lifecycles card-id card-props]))))
