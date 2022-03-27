
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.prototypes
    (:require [mid-fruits.candy                    :refer [param]]
              [plugins.item-browser.routes.helpers :as routes.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) browser-props
  ;  {:route-template (string)(opt)}
  ;
  ; @return (map)
  ;  {:base-route (string)
  ;   :extended-route (string)
  ;   :parent-route (string)}
  [browser-id {:keys [route-template] :as browser-props}]
  (merge {}
         (if route-template {:base-route     (routes.helpers/base-route     browser-id browser-props)
                             :extended-route (routes.helpers/extended-route browser-id browser-props)
                             :parent-route   (routes.helpers/parent-route   browser-id browser-props)})
         (param browser-props)))
