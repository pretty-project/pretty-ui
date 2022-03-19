
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
  ;
  ; @return (map)
  ;  {:base-route (string)
  ;   :extended-route (string)}
  [extension-id item-namespace browser-props]
  (merge {:base-route     (routes.helpers/base-route     extension-id item-namespace browser-props)
          :extended-route (routes.helpers/extended-route extension-id item-namespace browser-props)}
         (param browser-props)))
