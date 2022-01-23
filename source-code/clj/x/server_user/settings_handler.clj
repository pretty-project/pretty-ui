
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.24
; Description:
; Version: v0.7.4
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.settings-handler
    (:require [local-db.api       :as local-db]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]
              [x.server-db.api    :as db]
              [x.server-user.account-handler :as account-handler]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
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
  (let [user-settings (request->user-settings request)]
       (db/get-document-value user-settings item-id)))

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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upload-user-settings-item!
  ; @param (map) request
  ;  {:params {:item-id (keyword)
  ;            :item (*)}}
  ;
  ; @return (map)
  [request]
  (if (account-handler/request->authenticated? request)
      ; If user authenticated ...
      (let [user-id    (http/request->session-param request :user-account/id)
            item-key   (http/request->param         request :item-key)
            item-value (http/request->param         request :item-value)
            namespaced-item-key (keyword/add-namespace "user-settings" item-key)
            default-value       (get ANONYMOUS-USER-SETTINGS namespaced-item-key)]
           (if (= item-value default-value)
               (local-db/update-document! "user_settings" user-id dissoc item-key)
               (local-db/update-document! "user_settings" user-id assoc  item-key item-value))
           (http/text-wrap {:body "Uploaded"}))
      ; If user is NOT authenticated ...
      (http/error-wrap {:error-message :permission-denied :status 401})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- transfer-user-settings
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (-> request request->user-settings db/document->pure-document))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-init [:core/reg-transfer! :user/user-settings
                                        {:data-f      transfer-user-settings
                                         :target-path [:user/settings :data-items]}]})
