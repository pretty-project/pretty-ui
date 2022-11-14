
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.scroll-handler.state)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (boolean)
;  A scrolled-to-top? változásának megállapításához ...
(def SCROLLED-TO-TOP? (atom nil))
