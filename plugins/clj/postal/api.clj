
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns postal.api
    (:require [postal.email :as email]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; postal.email
(def send-email! email/send-email!)
