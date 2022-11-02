
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-dictionary.api
    (:require [x.app-dictionary.term-handler.lifecycles]
              [x.app-dictionary.term-handler.events       :as term-handler.events]
              [x.app-dictionary.term-handler.helpers      :as term-handler.helpers]
              [x.app-dictionary.term-handler.side-effects :as term-handler.side-effects]
              [x.app-dictionary.term-handler.subs         :as term-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-dictionary.term-handler.events
(def add-term!  term-handler.events/add-term!)
(def add-terms! term-handler.events/add-terms!)

; x.app-dictionary.term-handler.helpers
(def join-string term-handler.helpers/join-string)

; x.app.dictionary.term-handler.side-effects
(def looked-up term-handler.side-effects/looked-up)

; x.app.dictionary.term-handler.subs
(def get-term     term-handler.subs/get-term)
(def term-exists? term-handler.subs/term-exists?)
(def look-up      term-handler.subs/look-up)
