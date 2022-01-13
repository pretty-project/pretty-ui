
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.12
; Description:
; Version: v0.4.0
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.installer
    (:require [mongo-db.api         :as mongo-db]
             ;[prototypes.api       :as prototypes]
              [x.server-core.api    :as a :refer [r]]
              [x.server-user.engine :as engine]))



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
(def DEMO-USER-PROFILE-DOCUMENT {:user-profile/birthday      "2020-04-20"
                                 :user-profile/first-name    "Tech"
                                 :user-profile/last-name     "Mono"
                                 :user-profile/id             engine/DEMO-USER-ID
                                 :user-profile/permissions   {engine/DEMO-USER-ID "rw"}
                                 :user-profile/registered-at "2020-04-20T16:20:00.000Z"})

; @constant (namespaced map)
(def DEMO-USER-SETTINGS-DOCUMENT {:id                 engine/DEMO-USER-ID
                                  :permissions       {engine/DEMO-USER-ID "rw"}
                                  :selected-language :hu})

; @constant (namespaced map)
(def DEVELOPER-USER-ACCOUNT-DOCUMENT {:user-account/email-address "developer@monotech.hu"
                                      :user-account/id            engine/DEVELOPER-USER-ID
                                      :user-account/password      "mono"
                                      :user-account/pin           "0000"
                                      :user-account/roles         [engine/DEVELOPER-USER-ID]
                                      :user-account/permissions   {engine/DEVELOPER-USER-ID "rw"}})

; @constant (namespaced map)
(def DEVELOPER-USER-PROFILE-DOCUMENT {:user-profile/birthday      "2020-04-20"
                                      :user-profile/first-name    "Tech"
                                      :user-profile/last-name     "Mono"
                                      :user-profile/id             engine/DEVELOPER-USER-ID
                                      :user-profile/permissions   {engine/DEVELOPER-USER-ID "rw"}
                                      :user-profile/registered-at "2020-04-20T16:20:00.000Z"})

; @constant (namespaced map)
(def DEVELOPER-USER-SETTINGS-DOCUMENT {:id                 engine/DEVELOPER-USER-ID
                                       :permissions       {engine/DEVELOPER-USER-ID "rw"}
                                       :selected-language :hu})



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- prototype-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespace) keyword
  ;
  ; @return (function)
  [namespace])
  ;#(prototypes/added-document-prototype {:session engine/SYSTEM-ACCOUNT} namespace %))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- check-install!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  ; Installing demo user ...
  (mongo-db/insert-document! "user_accounts" DEMO-USER-ACCOUNT-DOCUMENT  {:prototype-f (prototype-f :user-account)})
  (mongo-db/insert-document! "user_profile"  DEMO-USER-PROFILE-DOCUMENT  {:prototype-f (prototype-f :user-profile)})
  (mongo-db/insert-document! "user_settings" DEMO-USER-SETTINGS-DOCUMENT {:prototype-f (prototype-f :user-settings)})
  ; Installing developer user ...
  (mongo-db/insert-document! "user_accounts" DEMO-USER-ACCOUNT-DOCUMENT  {:prototype-f (prototype-f :user-account)})
  (mongo-db/insert-document! "user_profile"  DEMO-USER-PROFILE-DOCUMENT  {:prototype-f (prototype-f :user-profile)})
  (mongo-db/insert-document! "user_settings" DEMO-USER-SETTINGS-DOCUMENT {:prototype-f (prototype-f :user-settings)}))

(a/reg-fx :user/check-install! check-install!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-launch {:user/check-install! nil}})
