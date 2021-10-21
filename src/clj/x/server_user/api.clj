
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.08.08
; Description:
; Version: v0.5.0
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.api
    (:require [x.server-user.account-handler  :as account-handler]
              [x.server-user.engine           :as engine]
              [x.server-user.profile-handler  :as profile-handler]
              [x.server-user.session-handler  :as session-handler]
              [x.server-user.settings-handler :as settings-handler]
              [x.server-user.user-handler     :as user-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-user.account-handler
(def authenticate                 account-handler/authenticate)
(def logout                       account-handler/logout)
(def request->user-account        account-handler/request->user-account)
(def request->user-public-account account-handler/request->user-public-account)
(def request->authenticated?      account-handler/request->authenticated?)

; x.server-user.engine
(def DEFAULT-PROFILE-PICTURE-URL    engine/DEFAULT-PROFILE-PICTURE-URL)
(def user-roles->user-identified?   engine/user-roles->user-identified?)
(def user-roles->user-unidentified? engine/user-roles->user-unidentified?)

; x.server-user.profile-handler
(def request->user-profile      profile-handler/request->user-profile)
(def request->user-profile-item profile-handler/request->user-profile-item)

; x.server-user.session-handler
(def session->session-valid? session-handler/session->session-valid?)
(def request->modify-props   session-handler/request->modify-props)
(def request->create-props   session-handler/request->create-props)
(def request->delete-props   session-handler/request->delete-props)
(def request->upload-props   session-handler/request->upload-props)

; x.server-user.settings-handler
(def request->user-settings      settings-handler/request->user-settings)
(def request->user-settings-item settings-handler/request->user-settings-item)
