
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.login-handler.routes
    (:require [mid-fruits.map                        :as map]
              [mongo-db.api                          :as mongo-db]
              [server-fruits.http                    :as http]
              [x.server-user.account-handler.config  :as account-handler.config]
              [x.server-user.account-handler.helpers :as account-handler.helpers]
              [x.server-user.login-handler.helpers   :as login-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn authenticate
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:transit-params (map)
  ;   {:email-address (string)
  ;    :password (string)}}
  ;
  ; @return (map)
  [request]
  (let [pattern             (login-handler.helpers/request->authenticator-pattern request)
        public-user-account (mongo-db/get-document-by-query "user_accounts" pattern account-handler.config/PUBLIC-USER-ACCOUNT-PROJECTION)]
       (if (map/nonempty? public-user-account)
           (http/text-wrap  {:body "Speak, friend, and enter" :session public-user-account})
           (http/error-wrap {:error-message :permission-denied :status 401}))))

(defn logout
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  [_]
  (http/text-wrap {:body "Good bye!" :session {}}))
