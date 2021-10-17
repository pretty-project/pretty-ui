
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.23
; Description:
; Version: v0.4.4
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.settings-handler
    (:require [mid-fruits.candy  :refer [param]]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-user.engine :as engine]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-user-settings
  ; @return (map)
  [db _]
  (get-in db (db/path ::settings)))

(defn get-user-settings-item
  ; @param (keyword) item-id
  ;
  ; @return (map)
  [db [_ item-id]]
  (get-in db (db/path ::settings item-id)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-user-settings-item!
  ; @param (keyword) item-id
  ; @param (*) item
  ;
  ; @return (map)
  [db [_ item-id item]]
  (assoc-in db (db/path ::settings item-id)
               (param item)))

(a/reg-event-db :x.app-user/set-user-settings-item! set-user-settings-item!)
