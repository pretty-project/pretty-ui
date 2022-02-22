
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.23
; Description:
; Version: v0.7.2
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.profile-handler
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.string :as string]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-user.engine :as engine]
              [x.mid-user.profile-handler :as profile-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-user.profile-handler
(def MAX-FIRST-NAME-LENGTH profile-handler/MAX-FIRST-NAME-LENGTH)
(def MAX-LAST-NAME-LENGTH  profile-handler/MAX-LAST-NAME-LENGTH)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-user-profile
  ; @usage
  ;  (r user/get-user-profile db)
  ;
  ; @return (map)
  [db _]
  (get-in db (db/path :user/profile)))

; @usage
;  [:user/get-user-profile]
(a/reg-sub :user/get-user-profile get-user-profile)

(defn get-user-profile-item
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r user/get-user-profile-item db :email-address)
  ;
  ; @return (map)
  [db [_ item-key]]
  (get-in db (db/path :user/profile item-key)))

; @usage
;  [:user/get-user-profile-item :email-address]
(a/reg-sub :user/get-user-profile-item get-user-profile-item)

(defn get-user-first-name
  ; @usage
  ;  (r user/get-user-first-name db)
  ;
  ; @return (string)
  [db _]
  (r get-user-profile-item db :first-name))

; @usage
;  [:user/get-user-first-name]
(a/reg-sub :user/get-user-first-name get-user-first-name)

(defn get-user-last-name
  ; @usage
  ;  (r user/get-user-last-name db)
  ;
  ; @return (string)
  [db _]
  (r get-user-profile-item db :last-name))

; @usage
;  [:user/get-user-last-name]
(a/reg-sub :user/get-user-last-name get-user-last-name)

(defn get-user-name
  ; @usage
  ;  (r user/get-user-name db)
  ;
  ; @return (string)
  [db _]
  (string/trim (str (r get-user-first-name db) " "
                    (r get-user-last-name  db))))

; @usage
;  [:user/get-user-name]
(a/reg-sub :user/get-user-name get-user-name)

(defn get-user-phone-number
  ; @usage
  ;  (r user/get-user-phone-number db)
  ;
  ; @return (string)
  [db _]
  (r get-user-profile-item db :phone-number))

; @usage
;  [:user/get-user-phone-number]
(a/reg-sub :user/get-user-phone-number get-user-phone-number)

(defn get-user-profile-picture-url
  ; @usage
  ;  (r user/get-user-profile-picture-url db)
  ;
  ; @return (string)
  [db _]
  (or nil engine/DEFAULT-PROFILE-PICTURE-URL))

; @usage
;  [:user/get-user-profile-picture-url]
(a/reg-sub :user/get-user-profile-picture-url get-user-profile-picture-url)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-user-profile-item!
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @usage
  ;  (r set-user-profile-item! db :last-name "Roger")
  ;
  ; @return (map)
  [db [_ item-key item-value]]
  (assoc-in db (db/path :user/profile item-key) item-value))
