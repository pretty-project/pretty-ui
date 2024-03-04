
(ns pretty-diagrams.circle-diagram.utils
    (:require [fruits.css.api                        :as css]
              [fruits.math.api                       :as math]
              [pretty-diagrams.circle-diagram.config :as circle-diagram.config]
              [pretty-diagrams.engine.api            :as pretty-diagrams.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-datum-pattern
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:max-value (number)
  ;  :strength (percentage)
  ;  :total-value (number)
  ;  ...}
  ; @param (integer) dex
  ; @param (*) datum
  ;
  ; @return (string)
  [id {:keys [max-value strength total-value] :as props} dex datum]
  (let [datum-value   (pretty-diagrams.engine/get-diagram-datum-value id props dex datum)
        data-limit    (max total-value max-value)
        datum-ratio   (math/percent data-limit datum-value)
        median-radius (/ (- circle-diagram.config/CIRCLE-DIAMETER strength) 2)
        circum        (* median-radius math/PI 2)
        dash-filled   (* circum (/ datum-ratio 100))
        dash-empty    (- circum dash-filled)]
       (str dash-filled " " dash-empty)))

(defn diagram-datum-transform
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:max-value (number)
  ;  :total-value (number)
  ;  ...}
  ; @param (integer) dex
  ; @param (*) datum
  ;
  ; @return (string)
  [id {:keys [max-value total-value] :as props} dex datum]
  (let [datum-offset    (pretty-diagrams.engine/get-diagram-datum-offset id props dex datum)
        data-limit      (max total-value max-value)
        offset-ratio    (math/percent data-limit datum-offset)
        rotation-angle  (math/percent->angle offset-ratio)
        corrected-angle (+ rotation-angle circle-diagram.config/ANGLE-CORRECTION)]
       (css/rotate corrected-angle)))
