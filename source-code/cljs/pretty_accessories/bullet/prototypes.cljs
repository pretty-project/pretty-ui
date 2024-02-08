
(ns pretty-accessories.bullet.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bullet-props-prototype
  ; @ignore
  ;
  ; @param (keyword) bullet-id
  ; @param (map) bullet-props
  ;
  ; @return (map)
  [_ bullet-props]
  (-> bullet-props (pretty-properties/default-background-color-props {:fill-color :muted})
                   (pretty-properties/default-border-props           {})
                   (pretty-properties/default-size-props             {:height :xxs :width :xxs})))
