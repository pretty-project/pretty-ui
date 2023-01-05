
(ns elements.breadcrumbs.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  [breadcrumbs-props]
  (merge {}
         (param breadcrumbs-props)))
