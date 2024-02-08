
(ns pretty-elements.adornment-group.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-props-prototype
  ; @ignore
  ;
  ; @param (integer) adornment-dex
  ; @param (map) adornment-props
  ;
  ; @return (map)
  [_ adornment-props]
  (-> adornment-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  [_ group-props]
  (-> group-props (pretty-properties/default-flex-props {:orientation :horizontal :overflow :scroll})))
