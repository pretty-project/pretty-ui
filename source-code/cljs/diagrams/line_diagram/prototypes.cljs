
(ns diagrams.line-diagram.prototypes
    (:require [diagrams.line-diagram.utils :as line-diagram.utils]
              [math.api                    :as math]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn section-props-prototype
  ; @ignore
  ;
  ; @param (map) section-props
  ;
  ; @return (map)
  ; {:color (keyword or string)}
  [section-props]
  (merge {:color :primary}
         (-> section-props)))

(defn diagram-props-prototype
  ; @ignore
  ;
  ; @param (map) diagram-props
  ; {:strength (integer)(opt)}
  ;
  ; @return (map)
  ; {:strength (px)
  ;  :total-value (integer)
  ;  :width (keyword)}
  [{:keys [strength] :as diagram-props}]
  (merge {:total-value (line-diagram.utils/diagram-props->total-value diagram-props)
          :width :auto}
         (-> diagram-props)
         (if strength {:strength (math/between! strength 1 6)}
                      {:strength 2})))
