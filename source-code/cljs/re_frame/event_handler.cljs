
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns re-frame.event-handler
    (:require [mid.re-frame.event-handler :as event-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.re-frame.event-handler
(def get-event-handlers         event-handler/get-event-handlers)
(def get-event-handler          event-handler/get-event-handler)
(def event-handler-registrated? event-handler/event-handler-registrated?)
