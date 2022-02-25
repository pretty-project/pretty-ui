
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.api
    (:require [x.server-user.installer.lifecycles]
              [x.server-user.installer.side-effects]
              [x.server-user.account-handler.lifecycles]
              [x.server-user.account-handler.routes]
              [x.server-user.account-handler.transfer]
              [x.server-user.profile-handler.transfer]
              [x.server-user.settings-handler.lifecycles]
              [x.server-user.settings-handler.routes]
              [x.server-user.settings-handler.transfer]
              [x.server-user.user-handler.engine]
              [x.server-user.account-handler.engine    :as account-handler.engine]
              [x.server-user.engine                    :as engine]
              [x.server-user.profile-handler.engine    :as profile-handler.engine]
              [x.server-user.session-handler.engine    :as session-handler.engine]
              [x.server-user.settings-handler.engine   :as settings-handler.engine]
              [x.server-user.user-handler.side-effects :as user-handler.side-effects]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-user.account-handler.engine
(def ANONYMOUS-USER-ACCOUNT       account-handler.engine/ANONYMOUS-USER-ACCOUNT)
(def SYSTEM-ACCOUNT               account-handler.engine/SYSTEM-ACCOUNT)
(def request->user-account        account-handler.engine/request->user-account)
(def request->user-public-account account-handler.engine/request->user-public-account)
(def request->authenticated?      account-handler.engine/request->authenticated?)

; x.server-user.engine
(def DEFAULT-PROFILE-PICTURE-URL    engine/DEFAULT-PROFILE-PICTURE-URL)
(def user-roles->user-identified?   engine/user-roles->user-identified?)
(def user-roles->user-unidentified? engine/user-roles->user-unidentified?)

; x.server-user.profile-handler.engine
(def request->user-profile      profile-handler.engine/request->user-profile)
(def request->user-profile-item profile-handler.engine/request->user-profile-item)

; x.server-user.session-handler.engine
(def session->session-valid? session-handler.engine/session->session-valid?)

; x.server-user.settings-handler.engine
(def request->user-settings      settings-handler.engine/request->user-settings)
(def request->user-settings-item settings-handler.engine/request->user-settings-item)

; x.server-user.user-handler.side-effects
(def add-user! user-handler.side-effects/add-user!)
