
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.profile-handler.helpers
    (:require [local-db.api                         :as local-db]
              [mid-fruits.candy                     :refer [return]]
              [mid-fruits.keyword                   :as keyword]
              [mid-fruits.map                       :as map]
              [server-fruits.http                   :as http]
              [x.server-core.api                    :as a]
              [x.server-user.profile-handler.config :as profile-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-account-id->user-profile
  ; @param (string) user-account-id
  ;
  ; @usage
  ;  (user/user-account-id->user-profile "my-account")
  ;
  ; @return (namespaced map)
  [user-account-id]
  (let [user-profile (local-db/get-document "user_profiles" user-account-id)]
       (map/add-namespace user-profile :user-profile)))

(defn user-account-id->user-profile-item
  ; @param (string) user-account-id
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (user/user-account-id->user-profile "my-account" :first-name)
  ;
  ; @return (*)
  [user-account-id item-key]
  (let [user-profile        (user-account-id->user-profile user-account-id)
        namespaced-item-key (keyword/add-namespace :user-profile item-key)]
       (get user-profile namespaced-item-key)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->user-profile
  ; @param (map) request
  ;
  ; @usage
  ;  (r user/request->user-profile-item {...})
  ;
  ; @return (namespaced map)
  [request]
  (if-let [account-id (http/request->session-param request :user-account/id)]
          (user-account-id->user-profile account-id)
          (return profile-handler.config/ANONYMOUS-USER-PROFILE)))

(defn request->user-profile-item
  ; @param (map) request
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r user/request->user-profile-item {...} :first-name)
  ;
  ; @return (*)
  [request item-key]
  (let [user-profile        (request->user-profile request)
        namespaced-item-key (keyword/add-namespace :user-profile item-key)]
       (get user-profile namespaced-item-key)))
