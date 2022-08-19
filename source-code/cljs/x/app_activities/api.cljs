
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-activities.api
    (:require [x.app-activities.time-handler.subs :as time-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-activities.time-handler.subs
(def get-actual-timestamp    time-handler.subs/get-actual-timestamp)
(def get-actual-elapsed-time time-handler.subs/get-actual-elapsed-time)
