
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
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:route-template (string)(opt)}
  ;
  ; @return (map)
  ;  {:base-route (string)
  ;   :extended-route (string)
  ;   :parent-route (string)}
  [extension-id item-namespace {:keys [route-template] :as browser-props}]
  (merge {}
         (if route-template {:base-route     (routes.helpers/base-route     extension-id item-namespace browser-props)
                             :extended-route (routes.helpers/extended-route extension-id item-namespace browser-props)
                             :parent-route   (routes.helpers/parent-route   extension-id item-namespace browser-props)})
         (param browser-props)))
