
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.api
    (:require [engines.item-editor.core.effects]
              [engines.item-editor.core.events]
              [engines.item-editor.core.subs]
              [engines.item-editor.download.resolvers]
              [engines.item-editor.routes.effects]
              [engines.item-editor.transfer.effects]
              [engines.item-editor.download.helpers :as download.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-editor.download.helpers
(def env->item-suggestions download.helpers/env->item-suggestions)
