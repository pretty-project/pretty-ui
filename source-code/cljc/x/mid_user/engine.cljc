
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.22
; Description:
; Version: v0.4.2
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-user.engine
    (:require [mid-fruits.vector :as vector]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-PROFILE-PICTURE-URL "/images/default-user-profile-picture.png")



;; -- Helpers -----------------------------------------------------------------
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
  (-> user-roles user-roles->user-identified? not))
