
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.api
    (:require [x.app-user.account-handler.effects]
              [x.app-user.account-handler.events]
              [x.app-user.account-handler.subs     :as account-handler.subs]
              [x.app-user.core.helpers             :as core.helpers]
              [x.app-user.profile-handler.config   :as profile-handler.config]
              [x.app-user.profile-handler.events   :as profile-handler.events]
              [x.app-user.profile-handler.subs     :as profile-handler.subs]
              [x.app-user.settings-handler.events  :as settings-handler.events]
              [x.app-user.settings-handler.subs    :as settings-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-user.account-handler.subs
(def get-user-id            account-handler.subs/get-user-id)
(def get-user-email-address account-handler.subs/get-user-email-address)
(def get-user-roles         account-handler.subs/get-user-roles)
(def user-has-role?         account-handler.subs/user-has-role?)
(def logged-in?             account-handler.subs/logged-in?)
(def logged-out?            account-handler.subs/logged-out?)
(def user-identified?       account-handler.subs/user-identified?)
(def user-unidentified?     account-handler.subs/user-unidentified?)
(def get-login-attempted-at account-handler.subs/get-login-attempted-at)
(def login-attempted?       account-handler.subs/login-attempted?)
(def client-locked?         account-handler.subs/client-locked?)

; x.app-user.core.helpers
(def user-roles->user-identified?   core.helpers/user-roles->user-identified?)
(def user-roles->user-unidentified? core.helpers/user-roles->user-unidentified?)

; x.app-user.profile-handler.config
(def DEFAULT-PROFILE-PICTURE-URL profile-handler.config/DEFAULT-PROFILE-PICTURE-URL)

; x.app-user.profile-handler.events
(def set-user-profile-item! profile-handler.events/set-user-profile-item!)

; x.app-user.profile-handler.subs
(def get-user-profile         profile-handler.subs/get-user-profile)
(def get-user-profile-item    profile-handler.subs/get-user-profile-item)
(def get-user-first-name      profile-handler.subs/get-user-first-name)
(def get-user-last-name       profile-handler.subs/get-user-last-name)
(def get-user-locale          profile-handler.subs/get-user-locale)
(def get-user-phone-number    profile-handler.subs/get-user-phone-number)
(def get-user-profile-picture profile-handler.subs/get-user-profile-picture)

; x.app-user.settings-handler.events
(def set-user-settings-item! settings-handler.events/set-user-settings-item!)
(def set-user-settings!      settings-handler.events/set-user-settings!)

; x.app-user.settings-handler.subs
(def get-user-settings       settings-handler.subs/get-user-settings)
(def get-user-settings-item  settings-handler.subs/get-user-settings-item)
