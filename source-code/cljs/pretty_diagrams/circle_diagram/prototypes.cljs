
(ns pretty-diagrams.circle-diagram.prototypes
    (:require [pretty-defaults.api :as pretty-defaults]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-props-prototype
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  [_ diagram-props]
  (-> diagram-props (pretty-defaults/use-default-values {:diameter 48 :strength 2})))
