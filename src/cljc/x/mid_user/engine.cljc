
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.22
; Description:
; Version: v0.3.6
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-user.engine
    (:require [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-PROFILE-PICTURE-URL "/images/default-user-profile-picture.png")



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-roles->user-identified?
  ; @param (vector) user-roles
  ;
  ; @return (boolean)
  [user-roles]
  (vector/nonempty? user-roles))

(defn user-roles->user-unidentified?
  ; @param (vector) user-roles
  ;
  ; @return (boolean)
  [user-roles]
  (not (user-roles->user-unidentified? user-roles)))
