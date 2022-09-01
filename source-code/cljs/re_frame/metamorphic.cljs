
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns re-frame.metamorphic
    (:require [mid.re-frame.metamorphic :as metamorphic]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.re-frame.metamorphic
(def metamorphic-handler->handler-f metamorphic/metamorphic-handler->handler-f)
(def metamorphic-event->effects-map metamorphic/metamorphic-event->effects-map)
(def metamorphic-event<-params      metamorphic/metamorphic-event<-params)
