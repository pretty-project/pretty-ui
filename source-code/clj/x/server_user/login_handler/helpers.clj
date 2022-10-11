
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.login-handler.helpers
    (:require [mongo-db.api               :as mongo-db]
              [server-fruits.hash         :as hash]
              [server-fruits.http         :as http]
              [x.server-user.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->authenticator-pattern
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:transit-params (map)
  ;   {:email-address (string)
  ;    :password (string)}}
  ;
  ; @return (namespaced map)
  ;  {:user-account/email-address (string)
  ;   :user-account/password (string)}
  [{{:keys [email-address password]} :transit-params}]
  ; WARNING
  ; A felhasználó dokumentumának adatbázisból való kikérésekor a megfeleltetési minta
  ; nem lehet üres térkép, különben az adatbázis visszatérhet az első dokumentummal!
  ; Szükséges védekezni a kliens-oldalról érkező üres bejelentkezési adatok ellen!
  {:user-account/email-address email-address :user-account/password (hash/hmac-sha256 password email-address)})

(defn request->authenticated?
  ; @param (map) request
  ;
  ; @return (boolean)
  [request]
  (let [account-id (http/request->session-param request :user-account/id)
        user-roles (http/request->session-param request :user-account/roles)]
       (and (core.helpers/user-roles->user-identified? user-roles)
            (mongo-db/document-exists? "user_accounts" account-id))))
