
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.api
    (:require [pathom.effects]
              [pathom.events :as events]
              [pathom.subs   :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; pathom.events
(def clear-query-response! events/clear-query-response!)

; pathom.subs
(def get-query-response subs/get-query-response)
(def get-query-answer   subs/get-query-answer)
