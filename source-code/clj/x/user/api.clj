
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.api
    (:require [x.user.account-handler.transfer]
              [x.user.installer]
              [x.user.login-handler.lifecycles]
              [x.user.login-handler.transfer]
              [x.user.profile-handler.transfer]
              [x.user.settings-handler.lifecycles]
              [x.user.settings-handler.side-effects]
              [x.user.settings-handler.transfer]
              [x.user.account-handler.config    :as account-handler.config]
              [x.user.account-handler.helpers   :as account-handler.helpers]
              [x.user.core.helpers              :as core.helpers]
              [x.user.document-handler.helpers  :as document-handler.helpers]
              [x.user.login-handler.helpers     :as login-handler.helpers]
              [x.user.profile-handler.config    :as profile-handler.config]
              [x.user.profile-handler.helpers   :as profile-handler.helpers]
              [x.user.session-handler.helpers   :as session-handler.helpers]
              [x.user.settings-handler.helpers  :as settings-handler.helpers]
              [x.user.settings-handler.subs     :as settings-handler.subs]
              [x.user.user-handler.side-effects :as user-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.user.account-handler.config
(def ANONYMOUS-USER-ACCOUNT account-handler.config/ANONYMOUS-USER-ACCOUNT)
(def SYSTEM-USER-ACCOUNT    account-handler.config/SYSTEM-USER-ACCOUNT)

; x.user.account-handler.helpers
(def user-account-id->user-account      account-handler.helpers/user-account-id->user-account)
(def user-account-id->user-account-item account-handler.helpers/user-account-id->user-account-item)
(def request->user-account-id           account-handler.helpers/request->user-account-id)
(def request->user-account              account-handler.helpers/request->user-account)
(def request->public-user-account       account-handler.helpers/request->public-user-account)

; x.user.core.helpers
(def user-roles->user-identified?   core.helpers/user-roles->user-identified?)
(def user-roles->user-unidentified? core.helpers/user-roles->user-unidentified?)

; x.user.document-handler.helpers
(def added-document-prototype      document-handler.helpers/added-document-prototype)
(def updated-document-prototype    document-handler.helpers/updated-document-prototype)
(def duplicated-document-prototype document-handler.helpers/duplicated-document-prototype)
(def fill-document                 document-handler.helpers/fill-document)

; x.user.login-handler.helpers
(def user-account-id->user-login login-handler.helpers/user-account-id->user-login)
(def request->user-login         login-handler.helpers/request->user-login)
(def request->public-user-login  login-handler.helpers/request->public-user-login)

; x.user.profile-handler.config
(def DEFAULT-PROFILE-PICTURE-URL profile-handler.config/DEFAULT-PROFILE-PICTURE-URL)

; x.user.profile-handler.helpers
(def user-account-id->user-profile      profile-handler.helpers/user-account-id->user-profile)
(def user-account-id->user-profile-item profile-handler.helpers/user-account-id->user-profile-item)
(def request->user-profile              profile-handler.helpers/request->user-profile)
(def request->public-user-profile       profile-handler.helpers/request->public-user-profile)
(def request->user-profile-item         profile-handler.helpers/request->user-profile-item)

; x.user.session-handler.helpers
(def session->session-valid? session-handler.helpers/session->session-valid?)
(def request->authenticated? session-handler.helpers/request->authenticated?)
(def request->root-user?     session-handler.helpers/request->root-user?)

; x.user.settings-handler.helpers
(def user-account-id->user-settings      settings-handler.helpers/user-account-id->user-settings)
(def user-account-id->user-settings-item settings-handler.helpers/user-account-id->user-settings-item)
(def request->user-settings              settings-handler.helpers/request->user-settings)
(def request->public-user-settings       settings-handler.helpers/request->public-user-settings)
(def request->user-settings-item         settings-handler.helpers/request->user-settings-item)

; x.user.settings-handler.subs
(def get-default-user-settings settings-handler.subs/get-default-user-settings)

; x.user.user-handler.side-effects
(def add-user! user-handler.side-effects/add-user!)
