
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.api
    (:require [x.server-user.account-handler.transfer]
              [x.server-user.install-handler.lifecycles]
              [x.server-user.install-handler.side-effects]
              [x.server-user.login-handler.lifecycles]
              [x.server-user.profile-handler.transfer]
              [x.server-user.settings-handler.lifecycles]
              [x.server-user.settings-handler.transfer]
              [x.server-user.account-handler.config    :as account-handler.config]
              [x.server-user.account-handler.helpers   :as account-handler.helpers]
              [x.server-user.core.helpers              :as core.helpers]
              [x.server-user.document-handler.helpers  :as document-handler.helpers]
              [x.server-user.login-handler.helpers     :as login-handler.helpers]
              [x.server-user.profile-handler.config    :as profile-handler.config]
              [x.server-user.profile-handler.helpers   :as profile-handler.helpers]
              [x.server-user.session-handler.helpers   :as session-handler.helpers]
              [x.server-user.settings-handler.helpers  :as settings-handler.helpers]
              [x.server-user.user-handler.side-effects :as user-handler.side-effects]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-user.account-handler.config
(def ANONYMOUS-USER-ACCOUNT account-handler.config/ANONYMOUS-USER-ACCOUNT)
(def SYSTEM-ACCOUNT         account-handler.config/SYSTEM-ACCOUNT)

; x.server-user.account-handler.helpers
(def user-account-id->user-account      account-handler.helpers/user-account-id->user-account)
(def user-account-id->user-account-item account-handler.helpers/user-account-id->user-account-item)
(def request->user-account-id           account-handler.helpers/request->user-account-id)
(def request->user-account              account-handler.helpers/request->user-account)
(def request->user-public-account       account-handler.helpers/request->user-public-account)

; x.server-user.core.helpers
(def user-roles->user-identified?   core.helpers/user-roles->user-identified?)
(def user-roles->user-unidentified? core.helpers/user-roles->user-unidentified?)

;  x.server-user.document-handler.helpers
(def added-document-prototype      document-handler.helpers/added-document-prototype)
(def updated-document-prototype    document-handler.helpers/updated-document-prototype)
(def duplicated-document-prototype document-handler.helpers/duplicated-document-prototype)
(def fill-document                 document-handler.helpers/fill-document)

; x.server-user.login-handler.helpers
(def request->authenticated? login-handler.helpers/request->authenticated?)

; x.server-user.profile-handler.config
(def DEFAULT-PROFILE-PICTURE-URL profile-handler.config/DEFAULT-PROFILE-PICTURE-URL)

; x.server-user.profile-handler.helpers
(def user-account-id->user-profile      profile-handler.helpers/user-account-id->user-profile)
(def user-account-id->user-profile-item profile-handler.helpers/user-account-id->user-profile-item)
(def request->user-profile              profile-handler.helpers/request->user-profile)
(def request->user-profile-item         profile-handler.helpers/request->user-profile-item)

; x.server-user.session-handler.helpers
(def session->session-valid? session-handler.helpers/session->session-valid?)

; x.server-user.settings-handler.helpers
(def user-account-id->user-settings      settings-handler.helpers/user-account-id->user-settings)
(def user-account-id->user-settings-item settings-handler.helpers/user-account-id->user-settings-item)
(def request->user-settings              settings-handler.helpers/request->user-settings)
(def request->user-settings-item         settings-handler.helpers/request->user-settings-item)

; x.server-user.user-handler.side-effects
(def add-user! user-handler.side-effects/add-user!)
