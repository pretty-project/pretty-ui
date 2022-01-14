;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.23
; Description:
; Version: v0.5.8
; Compatibility: x4.5.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-synchronizer
    (:require [mid-fruits.candy   :refer [param]]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]
              [x.server-db.api    :as db]
              [x.server-user.api  :as user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-sync-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (let [app-config    (a/subscribed [:core/get-app-config])
        site-config   (a/subscribed [:core/get-site-config])
        client-routes (a/subscribed [:router/get-client-routes])
        user-account  (user/request->user-public-account request)
        user-profile  (user/request->user-profile        request)
        user-settings (user/request->user-settings       request)]
       (http/map-wrap {:body {:app-config    (param app-config)
                              :site-config   (param site-config)
                              :client-routes (param client-routes)
                              :user-account  (db/document->non-namespaced-document user-account)
                              :user-profile  (db/document->pure-document           user-profile)
                              :user-settings (db/document->pure-document           user-settings)}})))
