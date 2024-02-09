
(ns pretty-elements.breadcrumbs.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bullet-props-prototype
  ; @ignore
  ;
  ; @param (integer) bullet-dex
  ; @param (map) bullet-props
  ;
  ; @return (map)
  [_ bullet-props]
  (-> bullet-props (pretty-properties/default-background-color-props {:fill-color :muted})))

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
  (-> breadcrumbs-props (pretty-properties/default-flex-props         {:gap :xs :orientation :horizontal :overflow :scroll})
                        (pretty-properties/default-size-props         {:size-unit :full-block})
                        (pretty-properties/default-wrapper-size-props {})))
