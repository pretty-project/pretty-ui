
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



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-settings-item-path
  ; @usage
  ;  (user/user-settings-item-path :my-settings-item)
  ;
  ; @return (item-path vector)
  [item-id]
  (db/path ::settings item-id))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-user-settings
  ; @usage
  ;  (r user/get-user-settings db)
  ;
  ; @return (map)
  [db _]
  (get-in db (db/path ::settings)))

(defn get-user-settings-item
  ; @param (keyword) item-id
  ;
  ; @usage
  ;  (r user/get-user-settings-item db :my-settings-item)
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
  ; @usage
  ;  (r user/set-user-settings-item! db :my-settings-item "My value")
  ;
  ; @return (map)
  [db [_ item-id item]]
  (assoc-in db (db/path ::settings item-id)
               (param item)))

; @usage
;  [:user/set-user-settings-item! :my-settings-item "My value"]
(a/reg-event-db :user/set-user-settings-item! set-user-settings-item!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :user/upload-user-settings-item!
  ; @param (keyword) item-id
  ; @param (*) item
  ;
  ; @usage
  ;  [:user/upload-user-settings-item! :my-settings-item "My value"]
  (fn [{:keys [db]} [_ item-id item]]
      {:db       (r set-user-settings-item! db item-id item)
       :dispatch [:sync/send-request! :user/upload-user-settings-item!
                                      {:method :post
                                       :params {:item-id item-id :item item}
                                       :uri    "/user/upload-user-settings-item"}]}))
