
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.24
; Description:
; Version: v0.6.8
; Compatibility: x4.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.settings-handler
    (:require [local-db.api       :as local-db]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.map     :as map]
              [server-fruits.http :as http]
              [x.server-db.api    :as db]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
;  {:user-settings/hotkeys-enabled? (boolean)
;   :user-settings/notification-bubbles-enabled? (boolean)
;   :user-settings/notification-sounds-enabled (boolean)
;   :user-settings/sending-error-reports? (boolean)
;   :user-settings/selected-language (string)
;   :user-settings/selected-theme (string)}
(def ANONYMOUS-USER-SETTINGS {:user-settings/hotkeys-enabled?              true
                              :user-settings/notification-bubbles-enabled? true
                              :user-settings/notification-sounds-enabled?  false
                              :user-settings/sending-error-reports?        true
                              :user-settings/selected-language             "en"
                              :user-settings/selected-theme                "light"})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-account-id->user-settings
  ; @param (string) user-account-id
  ;
  ; @example
  ;  (user/user-account-id->user-settings "my-account")
  ;
  ; @return (map)
  [user-account-id]
  ; Minden felhasználó alapbeállításai megegyeznek az anonymous felhasználó beállításaival
  (merge (param ANONYMOUS-USER-SETTINGS)
         (local-db/get-document "user_settings" user-account-id
                                {:additional-namespace :user-settings})))

(defn request->user-settings
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (if-let [account-id (http/request->session-param request :user-account/id)]
          (user-account-id->user-settings account-id)
          (return ANONYMOUS-USER-SETTINGS)))

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
        default-user-settings (param ANONYMOUS-USER-SETTINGS)
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
