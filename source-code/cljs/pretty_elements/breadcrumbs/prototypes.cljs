
(ns pretty-elements.breadcrumbs.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-props-prototype
  ; @ignore
  ;
  ; @param (integer) crumb-dex
  ; @param (map) crumb-props
  ;
  ; @return (map)
  [_ crumb-props]
  (-> crumb-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-props-prototype
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  [_ breadcrumbs-props]
  (-> breadcrumbs-props (pretty-elements.properties/default-bullet-props {:bullet-color :muted})
                        (pretty-elements.properties/default-flex-props   {:gap :xs :orientation :horizontal :overflow :scroll})
                        (pretty-elements.properties/default-size-props   {:height :content :width :content})))
