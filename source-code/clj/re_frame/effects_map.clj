
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns re-frame.effects-map
    (:require [mid.re-frame.effects-map :as effects-map]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.re-frame.effects-map
(def effects-map<-event     effects-map/effects-map<-event)
(def merge-effects-maps     effects-map/merge-effects-maps)
(def effects-map->handler-f effects-map/effects-map->handler-f)
