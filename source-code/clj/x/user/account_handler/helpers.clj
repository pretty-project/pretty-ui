
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.account-handler.helpers
    (:require [candy.api                     :refer [return]]
              [keyword.api                   :as keyword]
              [http.api                      :as http]
              [mongo-db.api                  :as mongo-db]
              [x.user.account-handler.config :as account-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-account-id->user-account
  ; @param (string) user-account-id
  ;
  ; @example
  ;  (user-account-id->user-account "my-account")
  ;  =>
  ;  {:user-account/id       "my-account"
  ;   :user-account/password "my-password"
  ;   ...}
  ;
  ; @return (namespaced map)
  [user-account-id]
  (mongo-db/get-document-by-id "user_accounts" user-account-id))

(defn user-account-id->user-account-item
  ; @param (string) user-account-id
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (user-account-id->user-account "my-account" :email-address)
  ;
  ; @return (*)
  [user-account-id item-key]
  (let [user-account        (user-account-id->user-account user-account-id)
        namespaced-item-key (keyword/add-namespace :user-account item-key)]
       (get user-account namespaced-item-key)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->user-link
  ; @param (map) request
  ;
  ; @return (map)
  ;  {:user-account/id (string)}
  [request]
  (if-let [user-account-id (http/request->session-param request :user-account/id)]
          {:user-account/id user-account-id}))

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
  (if-let [user-account-id (http/request->session-param request :user-account/id)]
          (mongo-db/get-document-by-id "user_accounts" user-account-id)
          (return account-handler.config/ANONYMOUS-USER-ACCOUNT)))

(defn request->public-user-account
  ; @param (map) request
  ;
  ; @return (namespaced map)
  [request]
  (if-let [user-account-id (http/request->session-param request :user-account/id)]
          (mongo-db/get-document-by-id "user_accounts" user-account-id
                                       {:projection account-handler.config/PUBLIC-USER-ACCOUNT-PROJECTION})
          (return account-handler.config/ANONYMOUS-USER-ACCOUNT)))
