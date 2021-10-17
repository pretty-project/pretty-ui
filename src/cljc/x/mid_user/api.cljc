
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
    (:require [x.mid-user.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-user.engine
(def UNIDENTIFIED-USER-ID           engine/UNIDENTIFIED-USER-ID)
(def UNIDENTIFIED-USER-ROLES        engine/UNIDENTIFIED-USER-ROLES)
(def DEFAULT-PROFILE-PICTURE-URL    engine/DEFAULT-PROFILE-PICTURE-URL)
(def user-id->user-unidentified?    engine/user-id->user-unidentified?)
(def user-id->user-identified?      engine/user-id->user-identified?)
(def user-roles->user-unidentified? engine/user-roles->user-unidentified?)
(def user-roles->user-identified?   engine/user-roles->user-identified?)
