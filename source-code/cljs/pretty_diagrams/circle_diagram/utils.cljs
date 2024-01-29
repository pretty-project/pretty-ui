
(ns pretty-diagrams.circle-diagram.utils
    (:require [fruits.css.api                        :as css]
              [fruits.math.api                       :as math]
              [pretty-diagrams.circle-diagram.config :as circle-diagram.config]
              [pretty-diagrams.engine.api :as pretty-diagrams.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-datum-transform
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:diameter (px)
  ;  :strength (px)}
  ; @param (integer) datum-dex
  ; @param (*) datum
  ;
  ; @return (string)
  [diagram-id {:keys [diameter strength] :as diagram-props} datum-dex datum]
  (let [data-limit      (pretty-diagrams.engine/get-diagram-data-limit   diagram-id diagram-props)
        datum-offset    (pretty-diagrams.engine/get-diagram-datum-offset diagram-id diagram-props datum-dex datum)
        offset-ratio    (math/percent data-limit datum-offset)
        rotation-angle  (math/percent->angle offset-ratio)
        corrected-angle (+ rotation-angle circle-diagram.config/ANGLE-CORRECTION)]
       (css/rotate corrected-angle)))

(defn diagram-datum-pattern
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:diameter (px)
  ;  :strength (px)}
  ; @param (integer) datum-dex
  ; @param (*) datum
  ;
  ; @return (string)
  [diagram-id {:keys [diameter strength] :as diagram-props} datum-dex datum]
  (let [data-limit  (pretty-diagrams.engine/get-diagram-data-limit  diagram-id diagram-props)
        datum-value (pretty-diagrams.engine/get-diagram-datum-value diagram-id diagram-props datum-dex datum)
        datum-ratio (math/percent data-limit datum-value)
        radius      (/ (- diameter strength) 2)
        circum      (* radius math/PI        2)
        dash-filled (* circum (/ datum-ratio 100))
        dash-empty  (- circum dash-filled)]
       (str dash-filled " " dash-empty)))
