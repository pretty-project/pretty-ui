
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.api
    (:require [x.mid-dictionary.term-handler.events :as term-handler.events]
              [x.mid-dictionary.term-handler.subs   :as term-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-dictionary.term-handler.events
(def add-term!  term-handler.events/add-term!)
(def add-terms! term-handler.events/add-terms!)

; x.mid-dictionary.term-handler.subs
(def get-term     term-handler.subs/get-term)
(def term-exists? term-handler.subs/term-exists?)
