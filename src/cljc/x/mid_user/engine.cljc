
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
(def UNIDENTIFIED-USER-ID        "Anonymous")

; @constant (string)
(def UNIDENTIFIED-USER-ROLE      "unidentified")

; @constant (string)
(def DEFAULT-PROFILE-PICTURE-URL "/images/default-user-profile-picture.png")



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-id->user-unidentified?
  ; @param (keyword) user-id
  ;
  ; @return (boolean)
  [user-id]
  (= (string/lowercase user-id)
     (string/lowercase UNIDENTIFIED-USER-ID)))

(defn user-id->user-identified?
  ; @param (keyword) user-id
  ;
  ; @return (boolean)
  [user-id]
  (and (string/nonempty? user-id)
       (not (user-id->user-unidentified? user-id))))

(defn user-roles->user-unidentified?
  ; @param (vector) user-roles
  ;
  ; @return (boolean)
  [user-roles]
  (vector/contains-item? user-roles UNIDENTIFIED-USER-ROLE))

(defn user-roles->user-identified?
  ; @param (vector) user-roles
  ;
  ; @return (boolean)
  [user-roles]
  (not (user-roles->user-unidentified? user-roles)))
