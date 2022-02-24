
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.account-handler.routes
    (:require [local-db.api         :as local-db]
              [mid-fruits.map       :as map]
              [server-fruits.http   :as http]
              [x.server-user.account-handler.engine :as account-handler.engine]))



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
  (let [pattern      (account-handler.engine/request->authenticator-pattern request)
        user-account (local-db/match-document "user_accounts" pattern
                                              {:additional-namespace :user-account})
        user-public-account (account-handler.engine/user-account->user-public-account user-account)]
       (if (map/nonempty? user-public-account)
           (http/text-wrap  {:body "Speak, friend, and enter" :session user-public-account})
           (http/error-wrap {:error-message :permission-denied :status 401}))))

(defn logout
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  [_]
  (http/text-wrap {:body "Good bye!" :session {}}))
