
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.events
    (:require [x.server-developer.api]
              [x.server-dictionary.api]
              [x.server-environment.api]
              [x.server-router.api]
              [x.server-views.api]
              [x.app-details     :as details]
              [x.server-core.api :as a :refer [r]]
              [x.server-db.api   :as db]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-server-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  ;
  ; @return (map)
  [db [_ server-props]]
  (assoc-in db [:boot-loader :server-handler/meta-items] server-props))
