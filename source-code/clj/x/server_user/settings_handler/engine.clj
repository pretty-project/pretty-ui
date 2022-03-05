
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.settings-handler.engine
    (:require [local-db.api       :as local-db]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]
              [server-fruits.http :as http]
              [x.server-db.api    :as db]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced map)
;  {:user-settings/notification-bubbles-enabled? (boolean)
;   :user-settings/notification-sounds-enabled (boolean)
;   :user-settings/sending-error-reports? (boolean)
;   :user-settings/selected-language (keyword)
;   :user-settings/selected-theme (keyword)
;   :user-settings/timezone-offset (?)}
(def ANONYMOUS-USER-SETTINGS {:user-settings/notification-bubbles-enabled? true
                              :user-settings/notification-sounds-enabled?  false
                              :user-settings/sending-error-reports?        true
                              :user-settings/selected-language             :en
                              :user-settings/selected-theme                :light
                              :user-settings/timezone-offset               0})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-account-id->user-settings
  ; @param (string) user-account-id
  ;
  ; @usage
  ;  (user/user-account-id->user-settings "my-account")
  ;
  ; @return (namespaced map)
  [user-account-id]
  (let [user-settings (local-db/get-document "user_settings" user-account-id)
        user-settings (db/document->namespaced-document user-settings :user-settings)]
       ; Minden felhasználó alapbeállításai megegyeznek az anonymous felhasználó beállításaival
       (merge ANONYMOUS-USER-SETTINGS user-settings)))

(defn request->user-settings
  ; @param (map) request
  ;
  ; @usage
  ;  (user/request->user-settings {...})
  ;
  ; @return (namespaced map)
  [request]
  (if-let [account-id (http/request->session-param request :user-account/id)]
          (user-account-id->user-settings account-id)
          (return ANONYMOUS-USER-SETTINGS)))

(defn request->user-settings-item
  ; @param (map) request
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (user/request->user-settings-item {...} :selected-language)
  ;
  ; @return (map)
  [request item-key]
  (let [user-settings       (request->user-settings request)
        namespaced-item-key (keyword/add-namespace :user-settings item-key)]
       (get user-settings namespaced-item-key)))

(defn request->extracted-user-settings
  ; @param (map) request
  ;
  ; @usage
  ;  (user/request->extracted-user-settings {...})
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
