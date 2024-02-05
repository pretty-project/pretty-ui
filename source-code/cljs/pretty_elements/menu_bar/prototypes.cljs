
(ns pretty-elements.menu-bar.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

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
  (-> bar-props (pretty-elements.properties/default-flex-props {:orientation :horizontal :overflow :scroll})
                (pretty-elements.properties/default-size-props {:height :content :width :content})))


                ;(assoc-in [:menu-item-default :menu-id] bar-id)))
