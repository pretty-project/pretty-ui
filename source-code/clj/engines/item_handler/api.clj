
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.api
    (:require [engines.item-handler.core.effects]
              [engines.item-handler.core.events]
              [engines.item-handler.core.subs]
              [engines.item-handler.download.resolvers]
              [engines.item-handler.routes.effects]
              [engines.item-handler.transfer.effects]
              [engines.item-handler.download.helpers :as download.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-handler.download.helpers
(def env->item-suggestions download.helpers/env->item-suggestions)
