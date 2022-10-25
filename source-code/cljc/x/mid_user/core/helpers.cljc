
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-user.core.helpers
    (:require [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-roles->user-identified?
  ; @param (vector) user-roles
  ;
  ; @usage
  ;  (user-roles->user-identified? [...])
  ;
  ; @return (boolean)
  [user-roles]
  (vector/nonempty? user-roles))

(defn user-roles->user-unidentified?
  ; @param (vector) user-roles
  ;
  ; @usage
  ;  (user-roles->user-unidentified? [...])
  ;
  ; @return (boolean)
  [user-roles]
  (-> user-roles user-roles->user-identified? not))
