
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.profile-handler.helpers
    (:require [candy.api                     :refer [return]]
              [http.api                      :as http]
              [keyword.api                   :as keyword]
              [mongo-db.api                  :as mongo-db]
              [x.user.profile-handler.config :as profile-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-account-id->user-profile
  ; @param (string) user-account-id
  ;
  ; @usage
  ;  (user-account-id->user-profile "my-account")
  ;
  ; @return (namespaced map)
  [user-account-id]
  (mongo-db/get-document-by-id "user_profiles" user-account-id))

(defn user-account-id->user-profile-item
  ; @param (string) user-account-id
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (user-account-id->user-profile "my-account" :first-name)
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
  ;  (request->user-profile-item {...})
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
  ;  (request->user-profile-item {...})
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
  ;  (request->user-profile-item {...} :first-name)
  ;
  ; @return (*)
  [request item-key]
  (let [user-profile        (request->user-profile request)
        namespaced-item-key (keyword/add-namespace :user-profile item-key)]
       (get user-profile namespaced-item-key)))