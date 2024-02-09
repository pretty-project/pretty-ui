
(ns pretty-elements.menu-bar.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-props-prototype
  ; @ignore
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;
  ; @return (map)
  [_ item-props]
  (-> item-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (map)
  [bar-id bar-props]
  (-> bar-props (pretty-properties/default-border-props       {})
                (pretty-properties/default-flex-props         {:orientation :horizontal :overflow :scroll})
                (pretty-properties/default-size-props         {:height :content :width :content :size-unit :double-block})
                (pretty-properties/default-wrapper-size-props {})))
