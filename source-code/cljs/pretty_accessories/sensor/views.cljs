
(ns pretty-accessories.sensor.views
    (:require [fruits.random.api :as random]
              [pretty-accessories.sensor.attributes :as sensor.attributes]
              [pretty-accessories.sensor.prototypes :as sensor.prototypes]
              [pretty-presets.engine.api :as pretty-presets.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sensor
  ; @ignore
  ;
  ; @param (keyword) sensor-id
  ; @param (map) sensor-props
  [sensor-id sensor-props]
  [:div (sensor.attributes/sensor-attributes sensor-id sensor-props)
        [:div (sensor.attributes/sensor-inner-attributes sensor-id sensor-props)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @description
  ; Sensor accessory for elements.
  ;
  ; @links Implemented properties
  ; [Animation properties](pretty-core/cljs/pretty-properties/api.html#animation-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ; [Visibility properties](pretty-core/cljs/pretty-properties/api.html#visibility-properties)
  ;
  ; @param (keyword)(opt) sensor-id
  ; @param (map) sensor-props
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-accessories/sensor.png)
  ; [sensor {:fill-color      :invert
  ;          :on-mouse-over-f (fn [_] ...)}]
  ([sensor-props]
   [view (random/generate-keyword) sensor-props])

  ([sensor-id sensor-props]
   ; @note (tutorials#parameterizing)
   (fn [_ sensor-props]
       (let [sensor-props (pretty-presets.engine/apply-preset       sensor-id sensor-props)
             sensor-props (sensor.prototypes/sensor-props-prototype sensor-id sensor-props)]
            [sensor sensor-id sensor-props]))))
