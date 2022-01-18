
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.17
; Description:
; Version: v0.6.8
; Compatibility: x4.5.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.session-handler
    (:require [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
;  {:user-account/id (nil)
;   :user-account/roles (vector)}
(def ANONYMOUS-SESSION {:user-account/id    nil
                        :user-account/roles []})



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn session->session-valid?
  ; @param (map) session
  ;  {:user-account/id (string)(opt)
  ;   :user-account/roles (vector)(opt)}
  ;
  ; @return (boolean)
  [{:user-account/keys [id roles]}]
  (and (string/nonempty? id)
       (vector/nonempty? roles)))
