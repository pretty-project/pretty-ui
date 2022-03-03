
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-dictionary.api
    (:require [x.server-dictionary.term-handler.effects]
              [x.server-dictionary.term-handler.engine]
              [x.server-dictionary.term-handler.lifecycles]
              [x.server-dictionary.term-handler.transfer]
              [x.mid-dictionary.term-handler.events          :as term-handler.events]
              [x.server-dictionary.term-handler.side-effects :as term-handler.side-effects]
              [x.server-dictionary.term-handler.subs         :as term-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-dictionary.term-handler.events
(def add-term!  term-handler.events/add-term!)
(def add-terms! term-handler.events/add-terms!)

; x.server-dictionary.term-handler.side-effects
(def looked-up term-handler.side-effects/looked-up)

; x.server-dictionary.term-handler.subs
(def get-term     term-handler.subs/get-term)
(def term-exists? term-handler.subs/term-exists?)
(def look-up      term-handler.subs/look-up)
