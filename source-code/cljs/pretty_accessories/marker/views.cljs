
(ns pretty-accessories.marker.views
    (:require [fruits.random.api :as random]
              [pretty-accessories.marker.attributes :as marker.attributes]
              [pretty-accessories.marker.prototypes :as marker.prototypes]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- marker
  ; @ignore
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  [marker-id marker-props]
  [:div (marker.attributes/marker-attributes marker-id marker-props)
        [:div (marker.attributes/marker-body-attributes marker-id marker-props)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  [marker-id marker-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    marker-id marker-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount marker-id marker-props))
                         :reagent-render         (fn [_ marker-props] [marker marker-id marker-props])}))

(defn view
  ; @param (keyword)(opt) marker-id
  ; @param (map) marker-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :position (keyword)(opt)
  ;  :position-base (keyword)(opt)
  ;  :position-method (keyword)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [marker {...}]
  ;
  ; @usage
  ; [marker :my-marker {...}]
  ([marker-props]
   [view (random/generate-keyword) marker-props])

  ([marker-id marker-props]
   ; @note (tutorials#parameterizing)
   (fn [_ marker-props]
       (let [marker-props (pretty-presets.engine/apply-preset       marker-id marker-props)
             marker-props (marker.prototypes/marker-props-prototype marker-id marker-props)]
            [view-lifecycles marker-id marker-props]))))
