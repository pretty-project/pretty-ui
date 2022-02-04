
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.03.08
; Description:
; Version: v0.3.4
; Compatibility: x4.5.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-user.api
    (:require [x.mid-user.engine          :as engine]
              [x.mid-user.profile-handler :as profile-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-user.engine
(def DEFAULT-PROFILE-PICTURE-URL    engine/DEFAULT-PROFILE-PICTURE-URL)
(def user-roles->user-identified?   engine/user-roles->user-identified?)
(def user-roles->user-unidentified? engine/user-roles->user-unidentified?)
