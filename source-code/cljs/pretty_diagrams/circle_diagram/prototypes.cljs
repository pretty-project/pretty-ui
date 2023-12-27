
(ns pretty-diagrams.circle-diagram.prototypes
    (:require [pretty-diagrams.circle-diagram.utils :as circle-diagram.utils]
              [pretty-build-kit.api :as pretty-build-kit]))

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
  ;
  ; @return (map)
  ; {:diameter (px)
  ;  :strength (px)}
  [diagram-props]
  ; @note (pretty-diagrams.circle-diagram.utils#1218)
  (-> diagram-props (pretty-build-kit/default-values {:diameter 48 :strength 2})
                    (circle-diagram.utils/diagram-props<-total-value)))
