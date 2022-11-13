
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.account-handler.subs
    (:require [mid-fruits.vector :as vector]
              [re-frame.api      :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-user-id
  ; @usage
  ;  (r get-user-id db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:user :account-handler/user-account :id]))

(defn get-user-email-address
  ; @usage
  ;  (r get-user-email-address db)
  ;
  ; @return (string)
  [db _]
  (get-in db [:user :account-handler/user-account :email-address]))

(defn get-user-roles
  ; @usage
  ;  (r get-user-roles db)
  ;
  ; @return (strings in vector)
  [db _]
  (get-in db [:user :account-handler/user-account :roles]))

(defn user-has-role?
  ; @param (string) user-role
  ;
  ; @usage
  ;  (r user-has-role? db)
  ;
  ; @return (boolean)
  [db [_ user-role]]
  (let [user-roles (r get-user-roles db)]
       (vector/contains-item? user-roles user-role)))

(defn client-locked?
  ; @usage
  ;  (r client-locked? db)
  ;
  ; @return (boolean)
  [db _]
  (let [client-locked? (get-in db [:user :account-handler/meta-items :client-locked?])]
       (boolean client-locked?)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:user/get-user-email-address]
(r/reg-sub :user/get-user-email-address get-user-email-address)

; @usage
;  [:user/client-locked?]
(r/reg-sub :user/client-locked? client-locked?)
