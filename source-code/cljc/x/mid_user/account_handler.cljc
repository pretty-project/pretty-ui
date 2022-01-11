
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.20
; Description:
; Version: v0.2.8
; Compatibility: x4.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-user.account-handler
    (:require [mid-fruits.form   :as form]
              [mid-fruits.string :as string]
              [x.mid-user.engine :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-account-valid?
  ; @param (map) user-account
  ;  {:user-account/email-address (string)
  ;   :user-account/password (string)
  ;   :user-account/pin (string)}
  ;
  ; @return (boolean)
  [{:user-account/keys [email-address password pin]}]
  (and (form/email-address-valid? email-address)
       (form/pin-valid?           pin)
       ; TEMP
       (string/nonempty?          password)))
      ;(form/password-valid?      password)
