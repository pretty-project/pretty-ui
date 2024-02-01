
(ns pretty-website.scroll-sensor.views
    (:require [fruits.random.api                         :as random]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [pretty-website.scroll-sensor.attributes   :as scroll-sensor.attributes]
              [pretty-website.scroll-sensor.side-effects :as scroll-sensor.side-effects]
              [pretty-website.scroll-sensor.prototypes :as scroll-sensor.prototypes]
              [reagent.api                               :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scroll-sensor
  ; @ignore
  ;
  ; @param (keyword) sensor-id
  ; @param (map) sensor-props
  [sensor-id sensor-props]
  [:div (scroll-sensor.attributes/sensor-attributes sensor-id sensor-props)])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) sensor-id
  ; @param (map) sensor-props
  ; {:callback-f (function)}
  [sensor-id {:keys [callback-f] :as sensor-props}]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (scroll-sensor.side-effects/sensor-did-mount-f sensor-id callback-f))
                       :reagent-render         (fn [_ sensor-props] [scroll-sensor sensor-id sensor-props])}))
                      ;on-mount? on-unmount?

(defn view
  ; @param (keyword)(opt) sensor-id
  ; @param (map) sensor-props
  ; {:callback-f (function)
  ;  :class (keyword or keywords in vector)(opt) -> class-attributes
  ;  :disabled? (boolean)(opt) -> make it really disabled -> turn off the callback-f! state-attributes
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)        -> style-attributes
  ;  :theme (keyword)(opt) ????}
  ;
  ; @usage
  ; [scroll-sensor {...}]
  ;
  ; @usage
  ; [scroll-sensor :my-sensor {...}]
  ;
  ; @usage
  ; (defn my-scroll-f [intersecting?] ...)
  ; [scroll-sensor {:callback-f my-scroll-f}]
  ([sensor-props]
   [view (random/generate-keyword) sensor-props])

  ([sensor-id sensor-props]
   ; @note (tutorials#parametering)
   (fn [_ sensor-props]
       (let [sensor-props (pretty-presets.engine/apply-preset              sensor-id sensor-props)
             sensor-props (scroll-sensor.prototypes/sensor-props-prototype sensor-id sensor-props)]
            [scroll-sensor sensor-id sensor-props]))))
