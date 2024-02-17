
(ns pretty-elements.breadcrumbs.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

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
  (-> bullet-props))

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
  (-> breadcrumbs-props (pretty-properties/default-flex-props {:gap :xs :orientation :horizontal :overflow :scroll})
                        (pretty-properties/default-size-props {:size-unit :full-block})
                        (pretty-standards/standard-flex-props)
                        (pretty-standards/standard-wrapper-size-props)
                        (pretty-rules/auto-adapt-wrapper-size)
                        (pretty-rules/auto-align-scrollable-flex)))
