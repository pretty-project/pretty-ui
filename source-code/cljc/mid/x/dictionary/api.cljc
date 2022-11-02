
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.dictionary.api
    (:require [mid.x.dictionary.term-handler.events  :as term-handler.events]
              [mid.x.dictionary.term-handler.helpers :as term-handler.helpers]
              [mid.x.dictionary.term-handler.subs    :as term-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.dictionary.term-handler.events
(def add-term!  term-handler.events/add-term!)
(def add-terms! term-handler.events/add-terms!)

; mid.x.dictionary.term-handler.helpers
(def join-string term-handler.helpers/join-string)

; mid.x.dictionary.term-handler.subs
(def get-term     term-handler.subs/get-term)
(def term-exists? term-handler.subs/term-exists?)
