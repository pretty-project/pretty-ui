
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns re-frame.context
    (:require [mid.re-frame.context :as context]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.re-frame.context
(def context->event-vector     context/context->event-vector)
(def context->event-id         context/context->event-id)
(def context->db-before-effect context/context->db-before-effect)
(def context->db-after-effect  context/context->db-after-effect)
