
(ns pretty-elements.chip.views
    (:require [fruits.random.api                     :as random]
              [fruits.vector.api                     :as vector]
              [pretty-elements.adornment-group.views :as adornment-group.views]
              [pretty-elements.chip.attributes       :as chip.attributes]
              [pretty-elements.chip.prototypes       :as chip.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {}
  [chip-id {:keys [end-adornments label start-adornments] :as chip-props}]
  [:div (chip.attributes/chip-attributes chip-id chip-props)
        [(pretty-elements.engine/clickable-auto-tag chip-id chip-props)
         (chip.attributes/chip-body-attributes      chip-id chip-props)
         (when (vector/not-empty? start-adornments)
               [adornment-group.views/view {:adornments start-adornments}])
         [:div (chip.attributes/chip-label-attributes chip-id chip-props) label]
         (when (vector/not-empty? end-adornments)
               [adornment-group.views/view {:adornments end-adornments}])]])

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
  ; @param (keyword)(opt) chip-id
  ; @param (map) chip-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;  :cursor (keyword or string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :end-adornments (maps in vector)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :height (keyword, px or string)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :hover-pattern (keyword)(opt)
  ;  :href-target (keyword)(opt)
  ;  :href-uri (string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :keypress (map)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-click-f (function)(opt)
  ;  :on-click-timeout (ms)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :start-adornments (maps in vector)(opt)
  ;  :style (map)(opt)
  ;  :tab-disabled? (boolean)(opt)
  ;  :text-color (keyword or string)(opt)
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
       (let [chip-props (pretty-presets.engine/apply-preset           chip-id chip-props)
             chip-props (chip.prototypes/chip-props-prototype         chip-id chip-props)
             chip-props (pretty-elements.engine/element-timeout-props chip-id chip-props)]
            [view-lifecycles chip-id chip-props]))))
