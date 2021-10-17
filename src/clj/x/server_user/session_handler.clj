
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.17
; Description:
; Version: v0.4.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.session-handler
    (:require [mid-fruits.candy     :refer [return]]
              [mid-fruits.map       :as map]
              [mid-fruits.string    :as string]
              [mid-fruits.vector    :as vector]
              [x.server-user.engine :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def DEFAULT-SESSION
     {:user-account/id    engine/UNIDENTIFIED-USER-ID
      :user-account/roles engine/UNIDENTIFIED-USER-ROLES})



;; -- Converters --------------------------------------------------------------
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

(defn request->session
  ; @param (map) request
  ;  {:session (map)(opt)}
  ;
  ; @return (map)
  ;  {:user-account/id (string)
  ;   :user-account/roles (vector)}
  [{:keys [session]}]
  (if (session->session-valid? session)
      (return session)
      (return DEFAULT-SESSION)))
