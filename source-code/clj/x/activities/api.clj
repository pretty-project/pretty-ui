
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.activities.api
    (:require [x.activities.channel-handler.side-effects :as channel-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.activities.channel-handler.side-effects
(def notifiy-client! channel-handler.side-effects/notifiy-client!)
