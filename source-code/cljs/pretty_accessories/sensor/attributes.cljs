
(ns pretty-accessories.sensor.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sensor-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) sensor-id
  ; @param (map) sensor-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ sensor-props]
  (-> {:class :pa-sensor--inner}
      (pretty-attributes/animation-attributes        sensor-props)
      (pretty-attributes/background-color-attributes sensor-props)
      (pretty-attributes/inner-size-attributes       sensor-props)
      (pretty-attributes/inner-space-attributes      sensor-props)
      (pretty-attributes/mouse-event-attributes      sensor-props)
      (pretty-attributes/style-attributes            sensor-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sensor-attributes
  ; @ignore
  ;
  ; @param (keyword) sensor-id
  ; @param (map) sensor-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ sensor-props]
  (-> {:class :pa-sensor}
      (pretty-attributes/class-attributes          sensor-props)
      (pretty-attributes/inner-position-attributes sensor-props)
      (pretty-attributes/outer-position-attributes sensor-props)
      (pretty-attributes/outer-size-attributes     sensor-props)
      (pretty-attributes/outer-space-attributes    sensor-props)
      (pretty-attributes/state-attributes          sensor-props)
      (pretty-attributes/theme-attributes          sensor-props)
      (pretty-attributes/visibility-attributes     sensor-props)))
