
(ns pretty-elements.surface.views
    (:require [dynamic-props.api                  :as dynamic-props]
              [fruits.random.api                  :as random]
              [pretty-elements.engine.api         :as pretty-elements.engine]
              [pretty-elements.surface.attributes :as surface.attributes]
              [pretty-elements.surface.config     :as surface.config]
              [pretty-elements.surface.prototypes :as surface.prototypes]
              [pretty-presets.engine.api          :as pretty-presets.engine]
              [reagent.api                        :as reagent]
              [transition-controller.api          :as transition-controller]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-controller
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:content (metamorphic-content)(opt)}
  [surface-id {:keys [content]}]
  ; The 'surface' element doesn't react on the changes of the ':content' property, unless it gets re-mounted,
  ; then the transition controller uses the given ':content' property as its initial content.
  [transition-controller/view surface-id {:initial-content     (if content [:div {:class :pe-surface--content} content])
                                          :transition-duration (-> surface.config/TRANSITION-DURATION)}])

(defn- surface
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:mounted? (boolean)(opt)}
  [surface-id {:keys [mounted?] :as surface-props}]
  (if mounted? [:div (surface.attributes/surface-attributes surface-id surface-props)
                     [:div (surface.attributes/surface-body-attributes surface-id surface-props)
                           [surface-controller                         surface-id surface-props]]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  [surface-id surface-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    surface-id surface-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount surface-id surface-props))
                       :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   surface-id surface-props %))
                       :reagent-render         (fn [_ surface-props] [surface surface-id surface-props])}))

(defn view
  ; @description
  ; Surface element for displaying content with optionally animated transitions,
  ; and additional controller functions.
  ;
  ; @param (keyword)(opt) surface-id
  ; @param (map) surface-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :layer (keyword or integer)(opt)
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :mounted? (boolean)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :position-base (keyword)(opt)
  ;  :position-method (keyword)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [surface {...}]
  ;
  ; @usage
  ; [surface :my-surface {...}]
  ([surface-props]
   [view (random/generate-keyword) surface-props])

  ([surface-id surface-props]
   ; @note (tutorials#parameterizing)
   (fn [_ surface-props]
       (let [surface-props (pretty-presets.engine/apply-preset         surface-id surface-props)
             surface-props (surface.prototypes/surface-props-prototype surface-id surface-props)
             surface-props (dynamic-props/import-props                 surface-id surface-props)]
            [view-lifecycles surface-id surface-props]))))
