
(ns elements.line-diagram.prototypes
    (:require [elements.line-diagram.helpers :as line-diagram.helpers]
              [noop.api                      :refer [param]]
              [math.api                      :as math]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn section-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) section-props
  ;
  ; @return (map)
  ; {:color (keyword or string)}
  [section-props]
  (merge {:color :primary}
         (param section-props)))

(defn diagram-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ; {:strength (integer)(opt)}
  ;
  ; @return (map)
  ; {:strength (px)
  ;  :total-value (integer)}
  [{:keys [strength] :as diagram-props}]
  (merge {:strength 2
          :total-value (line-diagram.helpers/diagram-props->total-value diagram-props)}
         (param diagram-props)
         (if strength {:strength (math/between! strength 1 6)})))
