

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.sample
    (:require [x.mid-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Ha a debug! interceptort használod, akkor az esemény megtörténésekor az esemény-vektor
; kiíródik a console/terminálra.
(a/reg-event-fx :my-event [a/debug!] (fn [_ _] [:my-event]))
