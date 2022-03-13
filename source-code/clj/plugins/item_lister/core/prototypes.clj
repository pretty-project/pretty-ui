
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
  ; @param (map) lister-props
  ;
  ; @return (map)
  ;  {:base-route (string)
  ;   :route-title (metamorphic-content)}
  [extension-id item-namespace lister-props]
  (merge {:base-route  (routes.helpers/base-route extension-id item-namespace lister-props)
          :route-title (param extension-id)}
         (param lister-props)))
