
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.prototypes
    (:require [mid-fruits.candy                   :refer [param]]
              [plugins.item-lister.routes.helpers :as routes.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn lister-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) lister-props
  ;  {:route-template (string)(opt)}
  ;
  ; @return (map)
  ;  {:base-route (string)}
  [lister-id {:keys [route-template] :as lister-props}]
  (merge {}
         (if route-template {:base-route (routes.helpers/base-route lister-id lister-props)})
         (param lister-props)))
