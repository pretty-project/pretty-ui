
(ns website.scroll-sensor.views
    (:require [hiccup.api                         :as hiccup]
              [random.api                         :as random]
              [reagent.api                        :as reagent]
              [website.scroll-sensor.side-effects :as scroll-sensor.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scroll-sensor
  ; @ignore
  ;
  ; @param (keyword) sensor-id
  ; @param (map) sensor-props
  ; {:callback-f (function)
  ;  :style (map)(opt)}
  [sensor-id {:keys [callback-f style]}]
  ; XXX#0106 (README.md#parametering)
  (reagent/lifecycles {:component-did-mount (fn [_ _] (scroll-sensor.side-effects/sensor-did-mount-f sensor-id callback-f))
                       :reagent-render      (fn [_ _]
                                                [:div {:class :w-scroll-sensor :id (hiccup/value sensor-id) :style style}])}))

(defn component
  ; @param (keyword)(opt) sensor-id
  ; @param (map) sensor-props
  ; {:callback-f (function)
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
   (fn [_ sensor-props] ; XXX#0106 (README.md#parametering)
       [scroll-sensor sensor-id sensor-props])))
