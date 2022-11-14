
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.print-handler.state
    (:require [x.core.print-handler.config :as print-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (ms)
(def SEPARATED-AT (atom print-handler.config/APP-STARTED-AT))

; @atom (integer)
(def SEPARATOR-NO (atom 0))
