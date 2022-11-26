
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.session-handler.helpers
    (:require [http.api            :as http]
              [mongo-db.api        :as mongo-db]
              [string.api          :as string]
              [vector.api          :as vector]
              [x.user.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn session->session-valid?
  ; @param (map) session
  ;  {:user-account/id (string)(opt)
  ;   :user-account/roles (vector)(opt)}
  ;
  ; @usage
  ;  (session->session-valid? {:user-account/id "my-user" :user-account/roles ["my-user"]})
  ;
  ; @return (boolean)
  [{:user-account/keys [id roles]}]
  (and (string/nonblank? id)
       (vector/nonempty? roles)))

(defn request->authenticated?
  ; @param (map) request
  ;
  ; @usage
  ;  (request->authenticated? {:session {:user-account/id "my-user" :user-account/roles ["my-user"]}})
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
  ; @usage
  ;  (request->root-user? {:session {:user-account/id "my-user" :user-account/roles ["my-user" "root"]}})
  ;
  ; @return (boolean)
  [request]
  (let [user-account-roles (http/request->session-param request :user-account/roles)]
       (vector/contains-item? user-account-roles "root")))
