
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns re-frame.coeffects
    (:require [mid.re-frame.coeffects :as coeffects]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.re-frame.coeffects
(def inject-cofx        coeffects/inject-cofx)
(def cofx->event-vector coeffects/cofx->event-vector)
(def cofx->event-id     coeffects/cofx->event-id)
