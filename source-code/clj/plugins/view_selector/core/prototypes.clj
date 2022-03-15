
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.prototypes
    (:require [mid-fruits.candy                     :refer [param]]
              [plugins.view-selector.routes.helpers :as routes.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;
  ; @return (map)
  ;  {:base-route (string)
  ;   :extended-route (string)
  ;   :parent-route (string)
  ;   :route-title (metamorphic-content)}
  [extension-id selector-props]
  (merge {:base-route     (routes.helpers/base-route     extension-id selector-props)
          :extended-route (routes.helpers/extended-route extension-id selector-props)
          :parent-route   (routes.helpers/parent-route   extension-id selector-props)
          :route-title    (param extension-id)}
         (param selector-props)))
