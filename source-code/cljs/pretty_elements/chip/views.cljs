
(ns pretty-elements.chip.views
    (:require [fruits.random.api                     :as random]
              [pretty-elements.adornment-group.views :as adornment-group.views]
              [pretty-elements.chip.attributes       :as chip.attributes]
              [pretty-elements.chip.prototypes       :as chip.prototypes]
              [pretty-elements.engine.api            :as pretty-elements.engine]
              [pretty-presets.engine.api             :as pretty-presets.engine]
              [pretty-accessories.api             :as pretty-accessories]
              [reagent.api                           :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {:cover (map)(opt)
  ;  :end-adornments (maps in vector)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :start-adornments (maps in vector)(opt)}
  [chip-id {:keys [cover end-adornments label start-adornments] :as chip-props}]
  [:div (chip.attributes/chip-attributes chip-id chip-props)
        [(pretty-elements.engine/clickable-auto-tag chip-id chip-props)
         (chip.attributes/chip-body-attributes      chip-id chip-props)
         (when start-adornments [adornment-group.views/view chip-id {:adornments start-adornments}])
         (when :always          [:div (chip.attributes/chip-label-attributes chip-id chip-props) label])
         (when end-adornments   [adornment-group.views/view chip-id {:adornments end-adornments}])
         (when cover            [pretty-accessories/cover   chip-id cover])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  [chip-id chip-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    chip-id chip-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount chip-id chip-props))
                       :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   chip-id chip-props %))
                       :reagent-render         (fn [_ chip-props] [chip chip-id chip-props])}))

(defn view
  ; @description
  ; Optionally clickable chip style element with adornments.
  ;
  ; @param (keyword)(opt) chip-id
  ; @param (map) chip-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;  :cover (map)(opt)
  ;  :cursor (keyword or string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :end-adornment-default (map)(opt)
  ;  :end-adornments (maps in vector)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;  :font-weight (keyword or integer)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :highlighted? (boolean)(opt)
  ;  :highlight-color (keyword or string)(opt)
  ;  :highlight-pattern (keyword)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :hover-pattern (keyword)(opt)
  ;  :href-target (keyword)(opt)
  ;  :href-uri (string)(opt)
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
  ;  :start-adornment-default (map)(opt)
  ;  :start-adornments (maps in vector)(opt)
  ;  :style (map)(opt)
  ;  :tab-disabled? (boolean)(opt)
  ;  :text-align (keyword)(opt)
  ;  :text-color (keyword or string)(opt)
  ;  :text-direction (keyword)(opt)
  ;  :text-overflow (keyword)(opt)
  ;  :text-selectable? (boolean)(opt)
  ;  :text-transform (keyword)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [chip {...}]
  ;
  ; @usage
  ; [chip :my-chip {...}]
  ([chip-props]
   [view (random/generate-keyword) chip-props])

  ([chip-id chip-props]
   ; @note (tutorials#parameterizing)
   (fn [_ chip-props]
       (let [chip-props (pretty-presets.engine/apply-preset                            chip-id chip-props)
             chip-props (chip.prototypes/chip-props-prototype                          chip-id chip-props)
             chip-props (pretty-elements.engine/element-timeout-props                  chip-id chip-props)
             chip-props (pretty-elements.engine/element-subitem-group<-subitem-default chip-id chip-props :start-adornments :start-adornment-default)
             chip-props (pretty-elements.engine/element-subitem-group<-subitem-default chip-id chip-props :start-adornments :start-adornment-default)
             chip-props (pretty-elements.engine/element-subitem-group<-disabled-state  chip-id chip-props :end-adornments)
             chip-props (pretty-elements.engine/element-subitem-group<-disabled-state  chip-id chip-props :end-adornments)]
            [view-lifecycles chip-id chip-props]))))
