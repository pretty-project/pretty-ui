
(ns pretty-elements.menu-bar.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (integer) button-dex
  ; @param (map) button-props
  ;
  ; @return (map)
  [_ button-props]
  (-> button-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (map)
  [_ bar-props]
  (-> bar-props (pretty-elements.properties/default-flex-props {:orientation :horizontal :overflow :scroll})
                (pretty-elements.properties/default-size-props {:height :content :width :content})))
