

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.register.state)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (vector)
(defonce HANDLERS    (atom {}))

; @atom (map)
(defonce ENVIRONMENT (atom {}))
