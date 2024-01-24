
(ns pretty-website.scroll-sensor.attributes
    (:require [fruits.hiccup.api    :as hiccup]
              [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sensor-attributes
  ; @ignore
  ;
  ; @param (keyword) sensor-id
  ; @param (map) sensor-props
  ;
  ; @return (map)
  [sensor-id sensor-props]
  (-> {:class :pw-scroll-sensor
       :id (hiccup/value sensor-id)}
      (pretty-css/style-attributes sensor-props)))
