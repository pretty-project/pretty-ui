
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.user.api
    (:require [mid.x.user.core.helpers           :as core.helpers]
              [mid.x.user.profile-handler.config :as profile-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.user.core.helpers
(def user-roles->user-identified?   core.helpers/user-roles->user-identified?)
(def user-roles->user-unidentified? core.helpers/user-roles->user-unidentified?)

; mid.x.user.profile-handler.config
(def DEFAULT-PROFILE-PICTURE-URL profile-handler.config/DEFAULT-PROFILE-PICTURE-URL)
