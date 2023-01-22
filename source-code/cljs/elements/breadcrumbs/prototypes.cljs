
(ns elements.breadcrumbs.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-props-prototype
  ; @ignore
  ;
  ; @param (map) crumb-props
  ; {}
  ;
  ; @return (map)
  [{:keys [href on-click] :as crumb-props}]
  (if (or href on-click)
      (assoc crumb-props :click-effect :opacity
                         :color        :default)
      (assoc crumb-props :color        :muted)))

(defn breadcrumbs-props-prototype
  ; @ignore
  ;
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  ; {}
  [breadcrumbs-props]
  (merge {:click-effect :opacity}
         (param breadcrumbs-props)))
