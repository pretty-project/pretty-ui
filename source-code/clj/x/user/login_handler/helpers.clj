
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.login-handler.helpers
    (:require [hash.api                      :as hash]
              [http.api                      :as http]
              [map.api                       :as map]
              [mongo-db.api                  :as mongo-db]
              [time.api                      :as time]
              [vector.api                    :as vector]
              [x.user.account-handler.config :as account-handler.config]
              [x.user.login-handler.config   :as login-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->authenticator-pattern
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:transit-params (map)
  ;   {:email-address (string)(opt)
  ;    :password (string)(opt)}}
  ;
  ; @return (namespaced map)
  ;  {:user-account/email-address (string)
  ;   :user-account/password (string)}
  [{{:keys [email-address password]} :transit-params}]
  ; WARNING#7760
  ; A felhasználó dokumentumának adatbázisból való kikérésekor a megfeleltetési minta
  ; nem lehet üres térkép, különben az adatbázis visszatérhet az első dokumentummal!
  ; Szükséges védekezni a kliens-oldalról érkező üres bejelentkezési adatok ellen!
  {:user-account/email-address email-address :user-account/password (if password (hash/hmac-sha256 password email-address))})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-login-attempt!
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:transit-params (map)
  ;   {:email-address (string)}
  ;
  ; @return (map)
  [{{:keys [email-address]} :transit-params}]
  ; XXX#4556
  ; Sikertelen bejelentkezéskor NEM BIZTOS, hogy létezik felhasználó a megadott email-címmel!
  (if-let [user-account (mongo-db/get-document-by-query "user_accounts" {:user-account/email-address email-address})]
          ; If the user exists with the given email-address ...
          (let [user-id   (:user-account/id user-account)
                timestamp (time/timestamp-string)]
               (if-let [user-login (mongo-db/get-document-by-id "user_logins" user-id)]
                       ; If the user has login history ...
                       (letfn [(f [%] (-> % (update :user-login/login-attempts vector/cons-item   timestamp)
                                            (update :user-login/login-attempts vector/first-items login-handler.config/MAX-LOGIN-ATTEMPT-LOG)))]
                              (mongo-db/apply-document! "user_logins" user-id f))
                       ; If the user has NO login history ...
                       (mongo-db/save-document! "user_logins" {:user-login/id user-id :user-login/login-attempts [timestamp]})))))

(defn reg-successful-login!
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:transit-params (map)
  ;   {:email-address (string)}
  ;
  ; @return (map)
  [{{:keys [email-address]} :transit-params}]
  ; XXX#4556
  ; Sikeres bejelentkezéskor BIZTOS, hogy létezik felhasználó a megadott email-címmel!
  (let [user-account (mongo-db/get-document-by-query "user_accounts" {:user-account/email-address email-address})
        user-id      (:user-account/id user-account)
        timestamp    (time/timestamp-string)]
       ; The user exists with the given email-address!
       (if-let [user-login (mongo-db/get-document-by-id "user_logins" user-id)]
               ; If the user has login history ...
               (letfn [(f [%] (-> % (update :user-login/successful-logins vector/cons-item   timestamp)
                                    (update :user-login/successful-logins vector/first-items login-handler.config/MAX-SUCCESSFUL-LOGIN-LOG)))]
                      (mongo-db/apply-document! "user_logins" user-id f))
               ; If the user has NO login history ...
               (mongo-db/save-document! "user_logins" {:user-login/id user-id :user-login/successful-logins [timestamp]}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn max-login-attempt-reached?
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:transit-params (map)
  ;   {:email-address (string)}
  ;
  ; @return (boolean)
  [{{:keys [email-address]} :transit-params}]
  ; XXX#4556
  ; Bejelentkezéskor NEM BIZTOS, hogy létezik felhasználó a megadott email-címmel!
  (if-let [user-account (mongo-db/get-document-by-query "user_accounts" {:user-account/email-address email-address})]
          ; If the user exists with the given email-address ...
          (let [user-id (:user-account/id user-account)]
               (if-let [user-login (mongo-db/get-document-by-id "user_logins" user-id)]
                       ; If the user has login history ...
                       (let [login-attempts (:user-login/login-attempts user-login)]
                            (and ; If the user reached the allowed count of login attempts ...
                                 (vector/min? login-attempts login-handler.config/MAX-LOGIN-ATTEMPT-ALLOWED)
                                 ; And the user reached the allowed count of login attempts in the expiration period ...
                                 (-> (vector/nth-item login-attempts (dec login-handler.config/MAX-LOGIN-ATTEMPT-ALLOWED))
                                     (time/timestamp-string->elapsed-s)
                                     (>= login-handler.config/LOGIN-ATTEMPT-EXPIRATION-WINDOW))))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn authenticate-f
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:transit-params (map)
  ;   {:email-address (string)
  ;    :password (string)}}
  ;
  ; @return (map)
  [request]
  (if (max-login-attempt-reached? request)
      (http/error-wrap {:error-message ":max-login-attempt-reached" :status 403})
      (let [authenticator-pattern (request->authenticator-pattern request)
            public-user-account   (mongo-db/get-document-by-query "user_accounts" authenticator-pattern account-handler.config/PUBLIC-USER-ACCOUNT-PROJECTION)]
           (if (map/nonempty? public-user-account)
               (do (reg-successful-login! request)
                   (http/text-wrap        {:body "Speak, friend, and enter" :session public-user-account}))
               (do (reg-login-attempt!    request)
                   (http/error-wrap       {:error-message ":permission-denied" :status 401}))))))

(defn logout-f
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  [_]
  (http/text-wrap {:body "Good bye!" :session {}}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-account-id->user-login
  ; @param (string) user-account-id
  ;
  ; @example
  ;  (user-account-id->user-login "my-account")
  ;  =>
  ;  {:user-login/id "my-account"
  ;   :user-login/login-attempts [...]
  ;   :user-login/successful-logins [...]}
  ;
  ; @return (namespaced map)
  [user-account-id]
  (mongo-db/get-document-by-id "user_logins" user-account-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->user-login
  ; @param (map) request
  ;
  ; @return (namespaced map)
  [request]
  (if-let [user-account-id (http/request->session-param request :user-account/id)]
          (mongo-db/get-document-by-id "user_logins" user-account-id)))

(defn request->public-user-login
  ; @param (map) request
  ;
  ; @return (namespaced map)
  [request]
  (if-let [user-account-id (http/request->session-param request :user-account/id)]
          (mongo-db/get-document-by-id "user_logins" user-account-id login-handler.config/PUBLIC-USER-LOGIN-PROJECTION)))
