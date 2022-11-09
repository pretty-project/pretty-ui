
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.postal.api
    (:require [plugins.postal.email :as email]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.postal.email
(def send-email! email/send-email!)
