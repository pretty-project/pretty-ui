
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.18
; Description:
; Version: v0.4.0
; Compatibility: x4.5.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.engine
    (:require [mid-fruits.random :as random]
              [x.mid-user.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-user.engine
(def DEFAULT-PROFILE-PICTURE-URL    engine/DEFAULT-PROFILE-PICTURE-URL)
(def user-roles->user-identified?   engine/user-roles->user-identified?)
(def user-roles->user-unidentified? engine/user-roles->user-unidentified?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn generate-pin
  ; @return (string)
  []
  (-> 4 random/generate-number str))
