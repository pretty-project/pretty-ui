
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.profile-handler.helpers
    (:require [mid-fruits.candy                     :refer [return]]
              [mid-fruits.keyword                   :as keyword]
              [mongo-db.api                         :as mongo-db]
              [server-fruits.http                   :as http]
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
  (mongo-db/get-document-by-id "user_profiles" user-account-id))

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
  ;  (user/request->user-profile-item {...})
  ;
  ; @return (namespaced map)
  [request]
  (if-let [user-account-id (http/request->session-param request :user-account/id)]
          (mongo-db/get-document-by-id "user_profiles" user-account-id)
          (return profile-handler.config/ANONYMOUS-USER-PROFILE)))

(defn request->public-user-profile
  ; @param (map) request
  ;
  ; @usage
  ;  (user/request->user-profile-item {...})
  ;
  ; @return (namespaced map)
  [request]
  (if-let [user-account-id (http/request->session-param request :user-account/id)]
          (mongo-db/get-document-by-id "user_profiles" user-account-id profile-handler.config/PUBLIC-USER-PROFILE-PROJECTION)
          (return profile-handler.config/ANONYMOUS-USER-PROFILE)))

(defn request->user-profile-item
  ; @param (map) request
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (user/request->user-profile-item {...} :first-name)
  ;
  ; @return (*)
  [request item-key]
  (let [user-profile        (request->user-profile request)
        namespaced-item-key (keyword/add-namespace :user-profile item-key)]
       (get user-profile namespaced-item-key)))
