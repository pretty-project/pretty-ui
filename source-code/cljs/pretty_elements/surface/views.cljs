
(ns pretty-elements.surface.views
    (:require [fruits.random.api :as random]
              [pretty-elements.surface.attributes :as surface.attributes]
              [pretty-elements.surface.env :as surface.env]
              [pretty-elements.surface.prototypes :as surface.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:content (metamorphic-content)(opt)}
  [surface-id {:keys [content] :as surface-props}]
  (if (surface.env/surface-visible? surface-id surface-props)
      [:div (surface.attributes/surface-attributes            surface-id surface-props)
            [:div (surface.attributes/surface-body-attributes surface-id surface-props)
                  (-> content)]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  [surface-id surface-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount  (fn [_ _] (pretty-elements.engine/element-did-mount  surface-id surface-props))
                       :component-did-update (fn [%]   (pretty-elements.engine/element-did-update surface-id surface-props %))
                       :reagent-render       (fn [_ surface-props] [surface surface-id surface-props])}))

(defn view
  ; @param (keyword)(opt) surface-id
  ; @param (map) surface-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :cursor (keyword or string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :orientation (keyword)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :vertical-align (keyword)(opt)
  ;  :visible? (boolean)(opt)
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
             surface-props (surface.prototypes/surface-props-prototype surface-id surface-props)]
            [view-lifecycles surface-id surface-props]))))
