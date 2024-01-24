
(ns pretty-diagrams.circle-diagram.prototypes
    (:require [pretty-defaults.api :as pretty-defaults]
              [pretty-diagrams.circle-diagram.utils :as circle-diagram.utils]))

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
  (-> diagram-props (pretty-defaults/use-default-values {:diameter 48 :strength 2})
                    (circle-diagram.utils/diagram-props<-total-value)))
