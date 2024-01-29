
(ns pretty-diagrams.circle-diagram.prototypes
    (:require [pretty-defaults.api :as pretty-defaults]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn section-props-prototype
  ; @ignore
  ;
  ; @param (integer) section-dex
  ; @param (map) section-props
  ;
  ; @return (map)
  ; {:color (keyword or string)}
  [_ section-props]
  (-> section-props (pretty-defaults/use-default-values {:color :primary})))
      ; + value 0

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-props-prototype
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  ; {:diameter (px)
  ;  :strength (px)}
  [_ diagram-props]
  ; @note (pretty-diagrams.circle-diagram.utils#1218)
  (-> diagram-props (pretty-defaults/use-default-values {:diameter 48 :strength 2})))
