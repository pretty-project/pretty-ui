
(ns pretty-elements.icon.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-props-prototype
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  [_ icon-props]
  (-> icon-props (pretty-properties/default-icon-props {:icon-size :m})))
