
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns re-frame.event-vector
    (:require [mid.re-frame.event-vector :as event-vector]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.re-frame.event-vector
(def event-vector->event-id    event-vector/event-vector->event-id)
(def event-vector->effects-map event-vector/event-vector->effects-map)
(def event-vector->handler-f   event-vector/event-vector->handler-f)
(def event-vector<-params      event-vector/event-vector<-params)
