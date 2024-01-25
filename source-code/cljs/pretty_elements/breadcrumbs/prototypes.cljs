
(ns pretty-elements.breadcrumbs.prototypes
    (:require [pretty-elements.core.props :as core.props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-props-prototype
  ; @ignore
  ;
  ; @param (map) crumb-props
  ;
  ; @return (map)
  [crumb-props]
  (-> crumb-props (core.props/font-props {:font-size :xs :font-weight :semi-bold})))

(defn breadcrumbs-props-prototype
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  [_ breadcrumbs-props]
  (-> breadcrumbs-props))
