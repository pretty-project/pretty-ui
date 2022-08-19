
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.account-handler.routes
    (:require [local-db.api                          :as local-db]
              [mid-fruits.map                        :as map]
              [server-fruits.http                    :as http]
              [x.server-db.api                       :as db]
              [x.server-user.account-handler.helpers :as account-handler.helpers]))



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
  (let [pattern      (account-handler.helpers/request->authenticator-pattern request)
        user-account (local-db/match-document "user_accounts" pattern)
        user-account (db/document->namespaced-document user-account :user-account)
        user-public-account (account-handler.helpers/user-account->user-public-account user-account)]
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
