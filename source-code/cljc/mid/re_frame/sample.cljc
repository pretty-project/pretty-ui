
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.re-frame.sample
    (:require [mid.re-frame.api :refer [debug! reg-event-fx]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Ha a debug! interceptort használod, akkor az esemény megtörténésekor az esemény-vektor
; kiíródik a console/terminálra.
(reg-event-fx :my-event [debug!] (fn [_ _] [:my-event]))
