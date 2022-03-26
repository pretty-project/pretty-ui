
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
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:route-template (string)(opt)}
  ;
  ; @return (map)
  ;  {:base-route (string)
  ;   :extended-route (string)
  ;   :parent-route (string)}
  [selector-id {:keys [route-template] :as selector-props}]
  (merge {}
         (if route-template {:base-route     (routes.helpers/base-route     selector-id selector-props)
                             :extended-route (routes.helpers/extended-route selector-id selector-props)
                             :parent-route   (routes.helpers/parent-route   selector-id selector-props)})
         (param selector-props)))
