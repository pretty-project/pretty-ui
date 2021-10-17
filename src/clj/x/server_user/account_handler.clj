
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.24
; Description:
; Version: v0.5.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.account-handler
    (:require [local-db.api       :as local-db]
              [mid-fruits.candy   :refer [param]]
              [mid-fruits.map     :as map]
              [ring.util.response :refer [redirect]]
              [server-fruits.http :as http]
              [x.mid-user.api     :as user]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
;  A felhasználó account dokumentumának mely kulcsai kerülhetnek
;  a HTTP Session térképbe.
(def USER-PUBLIC-ACCOUNT-PROPS
     [:user-account/email-address :user-account/id :user-account/roles])



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request->authenticator-pattern
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
  ;  => {:user-account/id "my-account"
  ;      :user-account/password "my-password"
  ;      ...}
  ;
  ; @return (map)
  [user-account-id]
  (merge (local-db/get-document "user_accounts" user/UNIDENTIFIED-USER-ID
                                {:additional-namespace :user-account})
         (local-db/get-document "user_accounts" user-account-id
                                {:additional-namespace :user-account})))

(defn user-account->user-public-account
  ; @param (map) user-account
  ;
  ; @return (map)
  [user-account]
  (map/inherit user-account USER-PUBLIC-ACCOUNT-PROPS))

(defn request->user-account
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (let [user-account-id (http/request->session-param request :user-account/id)]
       (user-account-id->user-account user-account-id)))

(defn request->user-public-account
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (let [user-account (request->user-account request)]
       (user-account->user-public-account user-account)))

(defn request->authenticated?
  ; @param (map) request
  ;
  ; @return (boolean)
  [request]
  (let [account-id (http/request->session-param request :user-account/id)]
       (and (user/user-id->user-identified? account-id)
            (local-db/document-exists? "user_accounts" account-id))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn authenticate
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:transit-params (map)
  ;   {:source (map)
  ;    {:email-address (string)
  ;     :password (string)}}}
  ;
  ; @return (map)
  [request]
  (let [pattern      (request->authenticator-pattern request)
        user-account (local-db/match-document "user_accounts" pattern
                                              {:additional-namespace :user-account})
        user-public-account (user-account->user-public-account user-account)]
       (if (map/nonempty? user-public-account)
           (http/text-wrap  {:body    (param "Speak, friend, and enter")
                             :session (param user-public-account)})
           (http/error-wrap {:error-message :permission-denied :status 401}))))

(defn logout
  ; @param (map) request
  ;
  ; @return (map)
  [_]
  (http/text-wrap {:body    (param "Good bye!")
                   :session (param {})}))
