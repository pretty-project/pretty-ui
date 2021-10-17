
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.24
; Description:
; Version: v0.4.4
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.profile-handler
    (:require [local-db.api       :as local-db]
              [mid-fruits.candy   :refer [param]]
              [server-fruits.http :as http]
              [x.mid-user.api     :as user]
              [x.server-db.api    :as db]))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->user-profile
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (let [account-id (http/request->session-param request :user-account/id)]
       (merge (local-db/get-document "user_profiles" user/UNIDENTIFIED-USER-ID
                                     {:additional-namespace :user-profile})
              (local-db/get-document "user_profiles" account-id
                                     {:additional-namespace :user-profile}))))

(defn request->user-profile-item
  ; @param (map) request
  ; @param (keyword) item-id
  ;
  ; @return (map)
  [request item-id]
  (let [user-profile (db/document->non-namespaced-document (request->user-profile request))]
       (get user-profile item-id)))
