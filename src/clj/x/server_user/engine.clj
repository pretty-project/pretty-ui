
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.18
; Description:
; Version: v0.3.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.engine
    (:require [x.mid-user.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-user.engine
(def UNIDENTIFIED-USER-ID           engine/UNIDENTIFIED-USER-ID)
(def UNIDENTIFIED-USER-ROLE         engine/UNIDENTIFIED-USER-ROLE)
(def DEFAULT-PROFILE-PICTURE-URL    engine/DEFAULT-PROFILE-PICTURE-URL)
(def user-id->user-unidentified?    engine/user-id->user-unidentified?)
(def user-id->user-identified?      engine/user-id->user-identified?)
(def user-roles->user-unidentified? engine/user-roles->user-unidentified?)
(def user-roles->user-identified?   engine/user-roles->user-identified?)
