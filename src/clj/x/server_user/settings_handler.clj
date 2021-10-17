
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.24
; Description:
; Version: v0.4.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.settings-handler
    (:require [local-db.api       :as local-db]
              [mid-fruits.candy   :refer [param]]
              [mid-fruits.map     :as map]
              [server-fruits.http :as http]
              [x.mid-user.api     :as user]
              [x.server-db.api    :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->user-settings
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (let [account-id (http/request->session-param request :user-account/id)]
       (merge (local-db/get-document "user_settings" user/UNIDENTIFIED-USER-ID
                                     {:additional-namespace :user-settings})
              (local-db/get-document "user_settings" account-id
                                     {:additional-namespace :user-settings}))))

(defn request->user-settings-item
  ; @param (map) request
  ; @param (keyword) item-id
  ;
  ; @return (map)
  [request item-id]
  (let [user-settings (db/document->non-namespaced-document (request->user-settings request))]
       (get user-settings item-id)))

(defn request->extracted-user-settings
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (let [account-id            (http/request->session-param request :user-account/id)
        ; Alapértelmezett beállítások
        default-user-settings (local-db/get-document "user_settings" user/UNIDENTIFIED-USER-ID)
        ; A felhasználó szerveren tárolt beállításai
        stored-user-settings  (local-db/get-document "user_settings" account-id)
        ; A felhasználó eszközéről érkezett beállítások
        posted-user-settings  (http/request->transit-param :abcd)
        ; A felhasználó szerveren tárolt beállításai a felhasználó eszközéről
        ; érkezett beállításokkal aktualizálva
        updated-user-settings (merge stored-user-settings posted-user-settings)]
       ; Az aktualizált beállításokból, kivonja az alapbeállításokat,
       ; hogy azok az elemek, amelyek megegyeznek a saját alapbeállításukkal,
       ; ne legyenek feleslegesen tárolva
       (map/difference updated-user-settings default-user-settings)))
