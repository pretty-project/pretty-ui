
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.account-handler.subs
    (:require [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-user.engine :as engine]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-user-id
  ; @usage
  ;  (r user/get-user-id db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:user :account-handler/data-items :id]))

(defn get-user-email-address
  ; @usage
  ;  (r user/get-user-email-address db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:user :account-handler/data-items :email-address]))

(defn get-user-roles
  ; @usage
  ;  (r user/get-user-roles db)
  ;
  ; @return (strings in vector)
  [db _]
  (get-in db [:user :account-handler/data-items :roles]))

(defn user-has-role?
  ; @param (string) user-role
  ;
  ; @usage
  ;  (r user/user-has-role? db)
  ;
  ; @return (boolean)
  [db [_ user-role]]
  (let [user-roles (r get-user-roles db)]
       (vector/contains-item? user-roles user-role)))

(defn logged-in?
  ; WARNING#4003
  ;  A rendszer a vendég felhasználó bejelentkezése esetén is bejelentkezettnek
  ;  tekinthető!
  ;  A felhasználói bejelentkezettség vizsgálatára használj (user-identified?)
  ;  függvényt!
  ;
  ; @usage
  ;  (r user/logged-in? db)
  ;
  ; @return (boolean)
  [db _]
  (let [user-id (r get-user-id db)]
       (some? user-id)))

(defn logged-out?
  ; WARNING#4003
  ;
  ; @usage
  ;  (r user/logged-out? db)
  ;
  ; @return (boolean)
  [db _]
  (let [user-id (r get-user-id db)]
       (nil? user-id)))

(defn user-identified?
  ; @usage
  ;  (r user/user-identified? db)
  ;
  ; @return (boolean)
  [db _]
  (let [user-roles (r get-user-roles db)]
       (engine/user-roles->user-identified? user-roles)))

(defn user-unidentified?
  ; @usage
  ;  (r user/user-unidentified? db)
  ;
  ; @return (boolean)
  [db _]
  (let [user-roles (r get-user-roles db)]
       (engine/user-roles->user-unidentified? user-roles)))

(defn get-login-attempted-at
  ; @usage
  ;  (r user/get-login-attempted-at db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:user :account-handler/meta-items :login-attempted-at]))

(defn login-attempted?
  ; @usage
  ;  (r user/login-attempted? db)
  ;
  ; @return (boolean)
  [db _]
  (let [login-attempted-at (r get-login-attempted-at db)]
       (some? login-attempted-at)))

(defn client-locked?
  ; @usage
  ;  (r user/client-locked? db)
  ;
  ; @return (boolean)
  [db _]
  (let [client-locked? (get-in db [:user :account-handler/meta-items :client-locked?])]
       (boolean client-locked?)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:user/user-unidentified?]
(a/reg-sub :user/user-unidentified? user-unidentified?)

; @usage
;  [:user/login-attempted?]
(a/reg-sub :user/login-attempted? login-attempted?)

; @usage
;  [:user/client-locked?]
(a/reg-sub :user/client-locked? client-locked?)
