
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.account-handler.helpers
    (:require [local-db.api                         :as local-db]
              [mid-fruits.candy                     :refer [return]]
              [mid-fruits.keyword                   :as keyword]
              [mid-fruits.map                       :as map]
              [server-fruits.http                   :as http]
              [x.server-user.account-handler.config :as account-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-account-id->user-account
  ; @param (string) user-account-id
  ;
  ; @example
  ;  (user/user-account-id->user-account "my-account")
  ;  =>
  ;  {:user-account/id "my-account"
  ;   :user-account/password "my-password"
  ;   ...}
  ;
  ; @return (namespaced map)
  [user-account-id]
  (let [user-account (local-db/get-document "user_accounts" user-account-id)]
       (map/add-namespace user-account :user-account)))

(defn user-account-id->user-account-item
  ; @param (string) user-account-id
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (user/user-account-id->user-account "my-account" :email-address)
  ;
  ; @return (*)
  [user-account-id item-key]
  (let [user-account        (user-account-id->user-account user-account-id)
        namespaced-item-key (keyword/add-namespace :user-account item-key)]
       (get user-account namespaced-item-key)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-account->user-public-account
  ; @param (map) user-account
  ;
  ; @return (namespaced map)
  [user-account]
  (select-keys user-account account-handler.config/USER-PUBLIC-ACCOUNT-PROPS))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->user-account-id
  ; @param (map) request
  ;
  ; @return (string)
  [request]
  (http/request->session-param request :user-account/id))

(defn request->user-account
  ; @param (map) request
  ;
  ; @return (namespaced map)
  [request]
  (if-let [user-account-id (request->user-account-id request )]
          (user-account-id->user-account user-account-id)
          (return account-handler.config/ANONYMOUS-USER-ACCOUNT)))

(defn request->user-public-account
  ; @param (map) request
  ;
  ; @return (namespaced map)
  [request]
  (let [user-account (request->user-account request)]
       (user-account->user-public-account user-account)))
