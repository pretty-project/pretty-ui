
(ns pretty-elements.breadcrumbs.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

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
  (-> breadcrumbs-props (pretty-properties/default-bullet-props {:bullet-color :muted})
                        (pretty-properties/default-flex-props   {:gap :xs :orientation :horizontal :overflow :scroll})
                        (pretty-properties/default-size-props   {})))
