
(ns pretty-diagrams.line-diagram.prototypes
    (:require [fruits.math.api                    :as math]
              [pretty-build-kit.api               :as pretty-build-kit]
              [pretty-diagrams.line-diagram.utils :as line-diagram.utils]))

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
  (-> section-props (pretty-build-kit/default-values {:color :primary})))

(defn diagram-props-prototype
  ; @ignore
  ;
  ; @param (map) diagram-props
  ; {:strength (integer)(opt)}
  ;
  ; @return (map)
  ; {:strength (px)
  ;  :total-value (integer)
  ;  :width (keyword, px or string)}
  [{:keys [strength] :as diagram-props}]
  (merge {:total-value (line-diagram.utils/diagram-props->total-value diagram-props)
          :width :auto}
         (-> diagram-props)
         (if strength {:strength (math/between! strength 1 6)}
                      {:strength 2})))
