
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.account-handler.helpers
    (:require [local-db.api                         :as local-db]
              [mid-fruits.candy                     :refer [param return]]
              [server-fruits.http                   :as http]
              [x.server-db.api                      :as db]
              [x.server-user.core.helpers           :as core.helpers]
              [x.server-user.account-handler.config :as account-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->authenticator-pattern
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:transit-params (map)
  ;   {:source (map)
  ;    {:email-address (string)
  ;     :password (string)}}}
  ;
  ; @return (map)
  ;  {:email-address (string)
  ;   :password (string)}
  [request]
  (let [email-address (http/request->source-param request :email-address)
        password      (http/request->source-param request :password)]
       {:email-address email-address
        :password      password}))

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
       (db/document->namespaced-document user-account :user-account)))

(defn user-account->user-public-account
  ; @param (map) user-account
  ;
  ; @return (namespaced map)
  [user-account]
  (select-keys user-account account-handler.config/USER-PUBLIC-ACCOUNT-PROPS))

(defn request->user-account
  ; @param (map) request
  ;
  ; @return (namespaced map)
  [request]
  (if-let [user-account-id (http/request->session-param request :user-account/id)]
          (user-account-id->user-account user-account-id)
          (return account-handler.config/ANONYMOUS-USER-ACCOUNT)))

(defn request->user-public-account
  ; @param (map) request
  ;
  ; @return (namespaced map)
  [request]
  (let [user-account (request->user-account request)]
       (user-account->user-public-account user-account)))

(defn request->authenticated?
  ; @param (map) request
  ;
  ; @return (boolean)
  [request]
  (let [account-id (http/request->session-param request :user-account/id)
        user-roles (http/request->session-param request :user-account/roles)]
       (and (core.helpers/user-roles->user-identified? user-roles)
            (local-db/document-exists? "user_accounts" account-id))))
