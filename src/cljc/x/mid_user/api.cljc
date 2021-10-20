
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.03.08
; Description:
; Version: v0.2.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-user.api
    (:require [x.mid-user.engine          :as engine]
              [x.mid-user.account-handler :as account-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-user.engine
(def UNIDENTIFIED-USER-ROLE         engine/UNIDENTIFIED-USER-ROLE)
(def DEFAULT-PROFILE-PICTURE-URL    engine/DEFAULT-PROFILE-PICTURE-URL)
(def user-roles->user-unidentified? engine/user-roles->user-unidentified?)
(def user-roles->user-identified?   engine/user-roles->user-identified?)

; x.mid-user.account-handler
(def user-account-valid? account-handler/user-account-valid?)
