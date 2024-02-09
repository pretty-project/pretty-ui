
(ns pretty-elements.adornment.views
    (:require [fruits.random.api                    :as random]
              [pretty-elements.adornment.attributes :as adornment.attributes]
              [pretty-elements.adornment.prototypes :as adornment.prototypes]
              [pretty-elements.engine.api           :as pretty-elements.engine]
              [pretty-presets.engine.api            :as pretty-presets.engine]
              [pretty-accessories.api            :as pretty-accessories]
              [reagent.api                          :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- adornment
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ; {:cover (keyword)(opt)
  ;  :icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)}
  [adornment-id {:keys [cover icon label] :as adornment-props}]
  [:div (adornment.attributes/adornment-attributes adornment-id adornment-props)
        [(pretty-elements.engine/clickable-auto-tag      adornment-id adornment-props)
         (adornment.attributes/adornment-body-attributes adornment-id adornment-props)
         (cond label [:div (adornment.attributes/adornment-label-attributes adornment-id adornment-props) label]
               icon  [:i   (adornment.attributes/adornment-icon-attributes  adornment-id adornment-props) icon])
         (when cover [:<>  [pretty-accessories/cover adornment-id cover]])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  [adornment-id adornment-props]
  ; @note (tutorials#parameterizing)
  ; @note (#8097)
  ; The 'element-did-update' function re-registers the keypress events when the element property map gets changed.
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    adornment-id adornment-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount adornment-id adornment-props))
                       :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   adornment-id adornment-props %))
                       :reagent-render         (fn [_ adornment-props] [adornment adornment-id adornment-props])}))

(defn view
  ; @description
  ; Downsized button element for adornment groups.
  ;
  ; @note
  ; The 'adornment' element implements ...
  ; ... the Pretty [anchor](pretty-core/cljs/pretty-properties/api.html#anchor-properties) properties.
  ; ... the Pretty [background color](pretty-core/cljs/pretty-properties/api.html#background-color-properties) properties.
  ; ... the Pretty [border](pretty-core/cljs/pretty-properties/api.html#border-properties) properties.
  ; ... the Pretty [class](pretty-core/cljs/pretty-properties/api.html#class-properties) properties.
  ; ... the Pretty [clickable state](pretty-core/cljs/pretty-properties/api.html#clickable-state-properties) properties.
  ; ... the Pretty [cursor](pretty-core/cljs/pretty-properties/api.html#cursor-properties) properties.
  ; ... the Pretty [effect](pretty-core/cljs/pretty-properties/api.html#effect-properties) properties.
  ; ... the Pretty [icon](pretty-core/cljs/pretty-properties/api.html#icon-properties) properties.
  ; ... the Pretty [keypress](pretty-core/cljs/pretty-properties/api.html#keypress-properties) properties.
  ; ... the Pretty [label](pretty-core/cljs/pretty-properties/api.html#label-properties) properties.
  ; ... the Pretty [lifecycle](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties) properties.
  ; ... the Pretty [mouse event](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties) properties.
  ; ... the Pretty [preset](pretty-core/cljs/pretty-properties/api.html#preset-properties) properties.
  ; ... the Pretty [progress](pretty-core/cljs/pretty-properties/api.html#progress-properties) properties.
  ; ... the Pretty [size](pretty-core/cljs/pretty-properties/api.html#size-properties) properties.
  ; ... the Pretty [space](pretty-core/cljs/pretty-properties/api.html#space-properties) properties.
  ; ... the Pretty [style](pretty-core/cljs/pretty-properties/api.html#style-properties) properties.
  ; ... the Pretty [text](pretty-core/cljs/pretty-properties/api.html#text-properties) properties.
  ; ... the Pretty [theme](pretty-core/cljs/pretty-properties/api.html#theme-properties) properties.
  ;
  ; @param (keyword)(opt) adornment-id
  ; @param (map) adornment-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;  :cover (map)(opt)
  ;  :cursor (keyword or string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;  :font-weight (keyword or integer)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :highlighted? (boolean)(opt)
  ;  :highlight-color (keyword or string)(opt)
  ;  :highlight-pattern (keyword)(opt)
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
  ;  :label-placeholder (metamorphic-content)(opt)
  ;  :letter-spacing (keyword, px or string)(opt)
  ;  :line-height (keyword, px or string)(opt)
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-click-f (function)(opt)
  ;  :on-click-timeout (ms)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :progress (percentage)(opt)
  ;  :progress-color (keyword or string)(opt)
  ;  :progress-direction (keyword)(opt)
  ;  :progress-duration (ms)(opt)
  ;  :style (map)(opt)
  ;  :tab-disabled? (boolean)(opt)
  ;  :text-align (keyword)(opt)
  ;  :text-color (keyword or string)(opt)
  ;  :text-direction (keyword)(opt)
  ;  :text-overflow (keyword)(opt)
  ;  :text-selectable? (boolean)(opt)
  ;  :text-transform (keyword)(opt)
  ;  :theme (keyword)(opt)
  ;  :tooltip (map)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [adornment {...}]
  ;
  ; @usage
  ; [adornment :my-adornment {...}]
  ([adornment-props]
   [view (random/generate-keyword) adornment-props])

  ([adornment-id adornment-props]
   ; @note (tutorials#parameterizing)
   (fn [_ adornment-props]
       (let [adornment-props (pretty-presets.engine/apply-preset             adornment-id adornment-props)
             adornment-props (adornment.prototypes/adornment-props-prototype adornment-id adornment-props)
             adornment-props (pretty-elements.engine/element-timeout-props   adornment-id adornment-props)]
            [view-lifecycles adornment-id adornment-props]))))
