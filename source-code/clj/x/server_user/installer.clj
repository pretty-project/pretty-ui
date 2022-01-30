
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.12
; Description:
; Version: v0.4.0
; Compatibility: x4.5.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.installer
    (:require [mongo-db.api         :as mongo-db]
              [x.server-core.api    :as a :refer [r]]
              [x.server-user.engine :as engine]
              [x.server-user.account-handler :as account-handler]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced map)
(def DEMO-USER-ACCOUNT-DOCUMENT {:user-account/email-address "demo@monotech.hu"
                                 :user-account/id            engine/DEMO-USER-ID
                                 :user-account/password      "mono"
                                 :user-account/pin           "0000"
                                 :user-account/roles         [engine/DEMO-USER-ID]
                                 :user-account/permissions   {engine/DEMO-USER-ID "rw"}})

; @constant (namespaced map)
(def DEMO-USER-PROFILE-DOCUMENT {:user-profile/birthday    "2020-04-20"
                                 :user-profile/first-name  "Tech"
                                 :user-profile/last-name   "Mono"
                                 :user-profile/id           engine/DEMO-USER-ID
                                 :user-profile/permissions {engine/DEMO-USER-ID "rw"}})

; @constant (namespaced map)
(def DEMO-USER-SETTINGS-DOCUMENT {:user-settings/id                 engine/DEMO-USER-ID
                                  :user-settings/permissions       {engine/DEMO-USER-ID "rw"}
                                  :user-settings/selected-language :hu})

; @constant (namespaced map)
(def DEVELOPER-USER-ACCOUNT-DOCUMENT {:user-account/email-address "developer@monotech.hu"
                                      :user-account/id            engine/DEVELOPER-USER-ID
                                      :user-account/password      "mono"
                                      :user-account/pin           "0000"
                                      :user-account/roles         [engine/DEVELOPER-USER-ID]
                                      :user-account/permissions   {engine/DEVELOPER-USER-ID "rw"}})

; @constant (namespaced map)
(def DEVELOPER-USER-PROFILE-DOCUMENT {:user-profile/birthday    "2020-04-20"
                                      :user-profile/first-name  "Tech"
                                      :user-profile/last-name   "Mono"
                                      :user-profile/id           engine/DEVELOPER-USER-ID
                                      :user-profile/permissions {engine/DEVELOPER-USER-ID "rw"}})

; @constant (namespaced map)
(def DEVELOPER-USER-SETTINGS-DOCUMENT {:user-settings/id                 engine/DEVELOPER-USER-ID
                                       :user-settings/permissions       {engine/DEVELOPER-USER-ID "rw"}
                                       :user-settings/selected-language :hu})



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- prototype-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespace) keyword
  ;
  ; @return (function)
  [namespace]
  #(mongo-db/added-document-prototype {:session account-handler/SYSTEM-ACCOUNT} namespace %))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- check-install!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  ; Installing demo user ...
  (if-not (mongo-db/get-document-by-id "user_accounts" engine/DEMO-USER-ID)
          (mongo-db/insert-document! "user_accounts" DEMO-USER-ACCOUNT-DOCUMENT  {:prototype-f (prototype-f :user-account)}))
  (if-not (mongo-db/get-document-by-id "user_profile" engine/DEMO-USER-ID)
          (mongo-db/insert-document! "user_profile"  DEMO-USER-PROFILE-DOCUMENT  {:prototype-f (prototype-f :user-profile)}))
  (if-not (mongo-db/get-document-by-id "user_settings" engine/DEMO-USER-ID)
          (mongo-db/insert-document! "user_settings" DEMO-USER-SETTINGS-DOCUMENT {:prototype-f (prototype-f :user-settings)}))
  ; Installing developer user ...
  (if-not (mongo-db/get-document-by-id "user_accounts" engine/DEVELOPER-USER-ID)
          (mongo-db/insert-document! "user_accounts" DEVELOPER-USER-ACCOUNT-DOCUMENT  {:prototype-f (prototype-f :user-account)}))
  (if-not (mongo-db/get-document-by-id "user_profile" engine/DEVELOPER-USER-ID)
          (mongo-db/insert-document! "user_profile"  DEVELOPER-USER-PROFILE-DOCUMENT  {:prototype-f (prototype-f :user-profile)}))
  (if-not (mongo-db/get-document-by-id "user_settings" engine/DEVELOPER-USER-ID)
          (mongo-db/insert-document! "user_settings" DEVELOPER-USER-SETTINGS-DOCUMENT {:prototype-f (prototype-f :user-settings)})))

(a/reg-fx :user/check-install! check-install!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-launch {:user/check-install! nil}})
