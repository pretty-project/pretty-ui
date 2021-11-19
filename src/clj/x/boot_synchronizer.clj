;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.23
; Description:
; Version: v0.3.6
; Compatibility: x4.2.6



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
  (let [app-details     (a/subscribed [:x.server-core/get-app-details])
        site-links      (a/subscribed [:x.server-core/get-site-links])
        storage-details (a/subscribed [:x.server-core/get-storage-details])
        reserved-routes (a/subscribed [:x.server-router/get-reserved-routes])
        user-account    (user/request->user-public-account request)
        user-profile    (user/request->user-profile        request)
        user-settings   (user/request->user-settings       request)]
       (http/map-wrap {:body {:app-details     (param app-details)
                              :site-links      (param site-links)
                              :storage-details (param storage-details)
                              :reserved-routes (param reserved-routes)
                              :user-account    (db/document->non-namespaced-document user-account)
                              :user-profile    (db/document->pure-document           user-profile)
                              :user-settings   (db/document->pure-document           user-settings)}})))
