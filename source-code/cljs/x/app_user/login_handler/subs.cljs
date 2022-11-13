
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.login-handler.subs
    (:require [re-frame.api                    :as r :refer [r]]
              [x.app-user.account-handler.subs :as account-handler.subs]
              [x.app-user.core.helpers         :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn logged-in?
  ; WARNING#4003
  ;  A rendszer a vendég felhasználó bejelentkezése esetén is bejelentkezettnek
  ;  tekinthető!
  ;  A felhasználói bejelentkezettség vizsgálatára használj (user-identified?)
  ;  függvényt!
  ;
  ; @usage
  ;  (r logged-in? db)
  ;
  ; @return (boolean)
  [db _]
  (let [user-id (r account-handler.subs/get-user-id db)]
       (some? user-id)))

(defn logged-out?
  ; WARNING#4003
  ;
  ; @usage
  ;  (r logged-out? db)
  ;
  ; @return (boolean)
  [db _]
  (let [user-id (r account-handler.subs/get-user-id db)]
       (nil? user-id)))

(defn user-identified?
  ; @usage
  ;  (r user-identified? db)
  ;
  ; @return (boolean)
  [db _]
  (let [user-roles (r account-handler.subs/get-user-roles db)]
       (core.helpers/user-roles->user-identified? user-roles)))

(defn user-unidentified?
  ; @usage
  ;  (r user-unidentified? db)
  ;
  ; @return (boolean)
  [db _]
  (let [user-roles (r account-handler.subs/get-user-roles db)]
       (core.helpers/user-roles->user-unidentified? user-roles)))

(defn get-login-failure
  ; @usage
  ;  (r get-login-failure db)
  ;
  ; @return (map)
  ;  {:status (integer)
  ;   :timestamp (string)}
  [db _]
  (get-in db [:user :login-handler/meta-items :login-failure]))

(defn login-failured?
  ; @usage
  ;  (r login-failured? db)
  ;
  ; @return (boolean)
  [db _]
  (let [login-failure (r get-login-failure db)]
       (some? login-failure)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:user/user-identified?]
(r/reg-sub :user/user-identified? user-identified?)

; @usage
;  [:user/get-login-failure]
(r/reg-sub :user/get-login-failure get-login-failure)

; @usage
;  [:user/login-failured?]
(r/reg-sub :user/login-failured? login-failured?)
