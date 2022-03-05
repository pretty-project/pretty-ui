
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.account-handler.engine
    (:require [local-db.api         :as local-db]
              [mid-fruits.candy     :refer [param return]]
              [server-fruits.http   :as http]
              [x.server-db.api      :as db]
              [x.server-user.engine :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
;  A felhasználó account dokumentumának mely kulcsai kerülhetnek
;  a HTTP Session térképbe.
(def USER-PUBLIC-ACCOUNT-PROPS [:user-account/email-address :user-account/id :user-account/roles])

; @constant (namespaced map)
;  {:user-account/email-address (nil)
;   :user-account/id (nil)
;   :user-account/roles (strings in vector)}
(def ANONYMOUS-USER-ACCOUNT {:user-account/email-address nil
                             :user-account/id            nil
                             :user-account/roles         []})

; @constant (namespaced map)
;  {:user-account/email-address (nil)
;   :user-account/id (string)
;   :user-account/roles (strings in vector)}
(def SYSTEM-ACCOUNT {:user-account/email-address nil
                     :user-account/id            "system"
                     :user-account/roles         []})



;; -- Helpers -----------------------------------------------------------------
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
  (select-keys user-account USER-PUBLIC-ACCOUNT-PROPS))

(defn request->user-account
  ; @param (map) request
  ;
  ; @return (namespaced map)
  [request]
  (if-let [user-account-id (http/request->session-param request :user-account/id)]
          (user-account-id->user-account user-account-id)
          (return ANONYMOUS-USER-ACCOUNT)))

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
       (and (engine/user-roles->user-identified? user-roles)
            (local-db/document-exists? "user_accounts" account-id))))
