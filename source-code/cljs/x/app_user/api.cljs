
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v0.5.6
; Compatibility: x4.5.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.api
    (:require [x.app-user.account-handler  :as account-handler]
              [x.app-user.engine           :as engine]
              [x.app-user.profile-handler  :as profile-handler]
              [x.app-user.settings-handler :as settings-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-user.account-handler
(def get-user-id            account-handler/get-user-id)
(def get-user-email-address account-handler/get-user-email-address)
(def get-user-roles         account-handler/get-user-roles)
(def user-has-role?         account-handler/user-has-role?)
(def logged-in?             account-handler/logged-in?)
(def logged-out?            account-handler/logged-out?)
(def user-identified?       account-handler/user-identified?)
(def user-unidentified?     account-handler/user-unidentified?)
(def get-last-login-attempt account-handler/get-last-login-attempt)
(def login-attempted?       account-handler/login-attempted?)
(def client-locked?         account-handler/client-locked?)

; x.app-user.engine
(def DEFAULT-PROFILE-PICTURE-URL    engine/DEFAULT-PROFILE-PICTURE-URL)
(def user-roles->user-identified?   engine/user-roles->user-identified?)
(def user-roles->user-unidentified? engine/user-roles->user-unidentified?)

; x.app-user.profile-handler
(def get-user-profile             profile-handler/get-user-profile)
(def get-user-profile-item        profile-handler/get-user-profile-item)
(def get-user-first-name          profile-handler/get-user-first-name)
(def get-user-last-name           profile-handler/get-user-last-name)
(def get-user-name                profile-handler/get-user-name)
(def get-user-phone-number        profile-handler/get-user-phone-number)
(def get-user-profile-picture-url profile-handler/get-user-profile-picture-url)
(def set-user-profile-item!       profile-handler/set-user-profile-item!)

; x.app-user.settings-handler
(def get-user-settings       settings-handler/get-user-settings)
(def get-user-settings-item  settings-handler/get-user-settings-item)
(def set-user-settings-item! settings-handler/set-user-settings-item!)
