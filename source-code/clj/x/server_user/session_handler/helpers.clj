
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.session-handler.helpers
    (:require [mid-fruits.string          :as string]
              [mid-fruits.vector          :as vector]
              [mongo-db.api               :as mongo-db]
              [server-fruits.http         :as http]
              [x.server-user.core.config  :as core.config]
              [x.server-user.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn session->session-valid?
  ; @param (map) session
  ;  {:user-account/id (string)(opt)
  ;   :user-account/roles (vector)(opt)}
  ;
  ; @return (boolean)
  [{:user-account/keys [id roles]}]
  (and (string/nonempty? id)
       (vector/nonempty? roles)))

(defn request->authenticated?
  ; @param (map) request
  ;
  ; @return (boolean)
  [request]
  (let [user-account-id    (http/request->session-param request :user-account/id)
        user-account-roles (http/request->session-param request :user-account/roles)]
       (and (core.helpers/user-roles->user-identified? user-account-roles)
            (mongo-db/document-exists? "user_accounts" user-account-id))))

(defn request->root-user?
  ; @param (map) request
  ;
  ; @return (boolean)
  [request]
  (let [user-account-roles (http/request->session-param request :user-account/roles)]
       (vector/contains-item? user-account-roles "root")))
