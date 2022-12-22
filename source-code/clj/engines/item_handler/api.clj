
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.api
    (:require [engines.item-handler.core.effects]
              [engines.item-handler.core.events]
              [engines.item-handler.download.resolvers]
              [engines.item-handler.transfer.effects]
              [engines.item-handler.core.subs        :as core.subs]
              [engines.item-handler.download.helpers :as download.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-handler.core.subs
(def get-handler-prop core.subs/get-handler-prop)

; engines.item-handler.download.helpers
(def env->item-suggestions download.helpers/env->item-suggestions)
