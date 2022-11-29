
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.api
    (:require [pathom.api]
              [engines.item-handler.backup.events]
              [engines.item-handler.body.effects]
              [engines.item-handler.body.events]
              [engines.item-handler.body.subs]
              [engines.item-handler.core.effects]
              [engines.item-handler.download.effects]
              [engines.item-handler.download.events]
              [engines.item-handler.download.subs]
              [engines.item-handler.routes.effects]
              [engines.item-handler.transfer.subs]
              [engines.item-handler.update.effects]
              [engines.item-handler.update.subs]
              [engines.item-handler.backup.subs :as backup.subs]
              [engines.item-handler.body.views  :as body.views]
              [engines.item-handler.core.events :as core.events]
              [engines.item-handler.core.subs   :as core.subs]
              [engines.item-handler.routes.subs :as routes.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-handler.backup.subs
(def item-changed? backup.subs/item-changed?)
(def form-changed? backup.subs/form-changed?)

; engines.item-handler.body.views
(def body body.views/body)

; engines.item-handler.core.events
(def set-meta-item! core.events/set-meta-item!)

; engines.item-handler.core.subs
(def get-meta-item       core.subs/get-meta-item)
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
(def export-current-item core.subs/export-current-item)
(def handling-item?      core.subs/handling-item?)

; engines.item-handler.routes.subs
(def get-item-route routes.subs/get-item-route)
