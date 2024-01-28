
(ns pretty-website.scroll-sensor.views
    (:require [fruits.random.api                         :as random]
              [pretty-engine.api                         :as pretty-engine]
              [pretty-presets.api                        :as pretty-presets]
              [pretty-website.scroll-sensor.attributes   :as scroll-sensor.attributes]
              [pretty-website.scroll-sensor.side-effects :as scroll-sensor.side-effects]
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

(defn- component-lifecycles
  ; @ignore
  ;
  ; @param (keyword) sensor-id
  ; @param (map) sensor-props
  ; {:callback-f (function)}
  [sensor-id {:keys [callback-f] :as sensor-props}]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (scroll-sensor.side-effects/sensor-did-mount-f sensor-id callback-f))
                       :reagent-render         (fn [_ sensor-props] [scroll-sensor sensor-id sensor-props])}))

(defn component
  ; @param (keyword)(opt) sensor-id
  ; @param (map) sensor-props
  ; {:callback-f (function)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)}
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
   [component (random/generate-keyword) sensor-props])

  ([sensor-id sensor-props]
   ; @note (tutorials#parametering)
   (fn [_ sensor-props]
       (let [sensor-props (pretty-presets/apply-preset sensor-props)]
             ; sensor-props (scroll-sensor.prototypes/sensor-props-prototype sensor-props)
            [scroll-sensor sensor-id sensor-props]))))
