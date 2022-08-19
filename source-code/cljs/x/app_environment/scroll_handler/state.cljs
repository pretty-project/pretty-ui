

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.scroll-handler.state)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (boolean)
;  A scrolled-to-top? változásának megállapításához ...
(def SCROLLED-TO-TOP? (atom nil))
