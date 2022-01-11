
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.23
; Description:
; Version: v0.7.4
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.account-handler
    (:require [mid-fruits.candy           :refer [param]]
              [mid-fruits.map             :refer [dissoc-in]]
              [mid-fruits.time            :as time]
              [mid-fruits.vector          :as vector]
              [x.app-core.api             :as a :refer [r]]
              [x.app-db.api               :as db]
              [x.app-user.engine          :as engine]
              [x.mid-user.account-handler :as account-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-user.account-handler
(def user-account-valid? account-handler/user-account-valid?)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-user-id
  ; @usage
  ;  (r user/get-user-id db)
  ;
  ; @return (string)
  [db _]
  (get-in db (db/path :user/account :id)))

(defn get-user-email-address
  ; @usage
  ;  (r user/get-user-email-address db)
  ;
  ; @return (string)
  [db _]
  (get-in db (db/path :user/account :email-address)))

(defn get-user-roles
  ; @usage
  ;  (r user/get-user-roles db)
  ;
  ; @return (strings in vector)
  [db _]
  (get-in db (db/path :user/account :roles)))

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

(defn get-last-login-attempt
  ; @usage
  ;  (r user/get-last-login-attempt db)
  ;
  ; @return (string)
  [db _]
  (get-in db (db/path :user/account :last-login-attempt)))

(defn login-attempted?
  ; @usage
  ;  (r user/login-attempted? db)
  ;
  ; @return (boolean)
  [db _]
  (let [last-login-attempt (r get-last-login-attempt db)]
       (some? last-login-attempt)))

(defn client-locked?
  ; @usage
  ;  (r user/client-locked? db)
  ;
  ; @return (boolean)
  [db _]
  (let [client-locked? (get-in db (db/path :user/account :client-locked?))]
       (boolean client-locked?)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- reg-last-login-attempt!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (assoc-in db (db/path :user/account :last-login-attempt)
               (time/elapsed)))

(a/reg-event-db :user/reg-last-login-attempt! reg-last-login-attempt!)

(defn- clear-last-login-attempt!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (dissoc-in db (db/path :user/account :last-login-attempt)))

(a/reg-event-db :user/clear-last-login-attempt! clear-last-login-attempt!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :user/authenticate!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A) Sikeres bejelentkezés
  ;    A bejelentkező felületen lévő bejelentkezés gombra kattintva, a gomb
  ;    várakozó állapotba lép. Ahhoz, hogy sikeres bejelentkezés után,
  ;    de még a rendszer indulása előtt, amíg látható a bejelentkező felület,
  ;    ne lehessen újból a bejelentkezés gombot megnyomni, szükséges
  ;    az {:idle-timeout ...} értékét az alapbeállításnál magasabbra állítani.
  ;
  ; B) Sikertelen bejelentkezés
  ;    A szerver válaszának megérkezésekor, sikertelen bejelentkezés esetén
  ;    egy hibaüzenet jelenik meg a bejelentkező felületen.
  ;    A szerver válaszának megérkezésekor elinduló {:idle-timeout ...} idő
  ;    letelte után lehetséges a bejelentkezés gombot újból megnyomni.
  [:sync/send-request! :user/authenticate!
                       {:method       :post
                        :on-success   [:boot-loader/restart-app!]
                        :on-failure   [:user/reg-last-login-attempt!]
                        :silent-mode? true
                        :source-path  [:login-box]
                        :uri          "/user/authenticate"
                        :idle-timeout 3000}])

(a/reg-event-fx
  :user/logout!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:sync/send-request! :user/logout!
                       {:method :post :uri "/user/logout"
                        :on-failure [:ui/blow-bubble!          {:content :logout-failed :color :warning}]
                        :on-success [:boot-loader/restart-app! {:restart-target "/login"}]}])
