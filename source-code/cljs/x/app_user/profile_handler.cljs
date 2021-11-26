
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.23
; Description:
; Version: v0.5.8
; Compatibility: x4.4.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.profile-handler
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.string :as string]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-user.engine :as engine]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-user-profile
  ; @return (map)
  [db _]
  (get-in db (db/path ::profile)))

(a/reg-sub :user/get-user-profile get-user-profile)

(defn get-user-profile-item
  ; @param (keyword) item-id
  ;
  ; @return (map)
  [db [_ item-id]]
  (get-in db (db/path ::profile item-id)))

(defn get-user-first-name
  ; @return (string)
  [db _]
  (r get-user-profile-item db :first-name))

(defn get-user-last-name
  ; @return (string)
  [db _]
  (r get-user-profile-item db :last-name))

(defn get-user-name
  ; @return (string)
  [db _]
  (string/trim (str (r get-user-first-name db)
                    (param string/tab)
                    (r get-user-last-name db))))

(defn get-user-phone-number
  [db _]
  (r get-user-profile-item db :phone-number))

(defn get-user-profile-picture-url
  ; @return (string)
  [db _]
  (or (return nil)
      (return engine/DEFAULT-PROFILE-PICTURE-URL)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-user-profile-item!
  ; @param (keyword) item-id
  ; @param (*) item
  ;
  ; @usage
  ;  (r set-user-profile-item! db :last-name "Roger")
  ;
  ; @return (map)
  [db [_ item-id item]]
  (assoc-in db (db/path ::profile item-id) item))
