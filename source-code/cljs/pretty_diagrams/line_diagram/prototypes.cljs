
(ns pretty-diagrams.line-diagram.prototypes
    (:require [fruits.math.api                    :as math]
              [pretty-defaults.api                :as pretty-defaults]
              [pretty-diagrams.engine.api                :as pretty-diagrams.engine]
              [pretty-diagrams.line-diagram.utils :as line-diagram.utils]
              [pretty-diagrams.core.props :as core.props]))

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
  (-> section-props (pretty-defaults/use-default-values {:color :primary})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-props-prototype
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  [diagram-id diagram-props]
  (-> diagram-props (core.props/row-props {})))




;  (merge {:total-value (line-diagram.utils/diagram-props->total-value diagram-props)
;          :width :auto
;         (-> diagram-props)
;         (if strength {:strength (math/between! strength 1 6)}
;                      {:strength 2})))
