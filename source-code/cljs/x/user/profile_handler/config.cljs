
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.profile-handler.config
    (:require [mid.x.user.profile-handler.config :as profile-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.user.profile-handler.config
(def DEFAULT-PROFILE-PICTURE-URL profile-handler.config/DEFAULT-PROFILE-PICTURE-URL)
(def MAX-FIRST-NAME-LENGTH       profile-handler.config/MAX-FIRST-NAME-LENGTH)
(def MAX-LAST-NAME-LENGTH        profile-handler.config/MAX-LAST-NAME-LENGTH)
