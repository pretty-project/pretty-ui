
(ns pretty-elements.adornment.views
    (:require [fruits.random.api                    :as random]
              [pretty-elements.adornment.attributes :as adornment.attributes]
              [pretty-elements.adornment.prototypes :as adornment.prototypes]
              [pretty-engine.api                    :as pretty-engine]
              [pretty-presets.api                   :as pretty-presets]
              [reagent.api                          :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- adornment
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ; {:href-uri (string)(opt)
  ;  :icon (keyword)(opt)
  ;  :label (string)(opt)
  ;  :on-click-f (function)(opt)}
  [adornment-id {:keys [href-uri icon label on-click-f] :as adornment-props}]
  [:div (adornment.attributes/adornment-attributes adornment-id adornment-props)
        [(cond href-uri :a on-click-f :button :else :div)
         (adornment.attributes/adornment-body-attributes adornment-id adornment-props)
         (cond label [:div (adornment.attributes/adornment-label-attributes adornment-id adornment-props) label]
               icon  [:i   (adornment.attributes/adornment-icon-attributes  adornment-id adornment-props) icon])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-lifecycles
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  [adornment-id adornment-props]
  ; @note (tutorials#parametering)
  ; @note (pretty-engine.element.lifecycles.side-effects#8097)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-engine/element-did-mount    adornment-id adornment-props))
                       :component-will-unmount (fn [_ _] (pretty-engine/element-will-unmount adornment-id adornment-props))
                       :component-did-update   (fn [%]   (pretty-engine/element-did-update   adornment-id adornment-props %))
                       :reagent-render         (fn [_ adornment-props] [adornment adornment-id adornment-props])}))

(defn element
  ; @param (keyword)(opt) adornment-id
  ; @param (map) adornment-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;  :cursor (keyword or string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-pattern (keyword)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :href-target (keyword)(opt)
  ;  :href-uri (string)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-color (keyword or string)(opt)
  ;  :icon-family (keyword)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :keypress (map)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-click-f (function)(opt)
  ;  :on-click-timeout (ms)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-mouse-over-f (function)(opt)
  ;  :on-right-click-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :progress (percent)(opt)
  ;  :progress-color (keyword or string)(opt)
  ;  :progress-direction (keyword)(opt)
  ;  :progress-duration (ms)(opt)
  ;  :style (map)(opt)
  ;  :tab-disabled? (boolean)(opt)
  ;  :text-color (keyword or string)(opt)
  ;  :text-overflow (keyword)(opt)
  ;  :text-transform (keyword)(opt)
  ;  :theme (keyword)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)
  ;  :tooltip-position (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [adornment {...}]
  ;
  ; @usage
  ; [adornment :my-adornment {...}]
  ([adornment-props]
   [element (random/generate-keyword) adornment-props])

  ([adornment-id adornment-props]
   ; @note (tutorials#parametering)
   (fn [_ adornment-props]
       (let [adornment-props (pretty-presets/apply-preset                    adornment-id adornment-props)
             adornment-props (adornment.prototypes/adornment-props-prototype adornment-id adornment-props)
             adornment-props (pretty-engine/element-timeout-props            adornment-id adornment-props)]
            [element-lifecycles adornment-id adornment-props]))))
