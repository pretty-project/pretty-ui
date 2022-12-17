
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.api
    (:require [pathom.api]
              [engines.item-viewer.backup.events]
              [engines.item-viewer.backup.subs]
              [engines.item-viewer.body.effects]
              [engines.item-viewer.body.events]
              [engines.item-viewer.core.effects]
              [engines.item-viewer.download.effects]
              [engines.item-viewer.download.events]
              [engines.item-viewer.download.subs]
              [engines.item-viewer.routes.effects]
              [engines.item-viewer.transfer.subs]
              [engines.item-viewer.update.effects]
              [engines.item-viewer.update.events]
              [engines.item-viewer.update.subs]
              [engines.item-viewer.body.subs   :as body.subs]
              [engines.item-viewer.body.views  :as body.views]
              [engines.item-viewer.core.events :as core.events]
              [engines.item-viewer.core.subs   :as core.subs]
              [engines.item-viewer.routes.subs :as routes.subs]
              [engines.item-viewer.update.subs :as update.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-viewer.body.subs
(def body-did-mount? body.subs/body-did-mount?)

; engines.item-viewer.body.views
(def body body.views/body)

; engines.item-viewer.core.events
(def set-item-id! core.events/set-item-id!)

; engines.item-viewer.core.subs
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
(def export-current-item core.subs/export-current-item)
(def viewing-item?       core.subs/viewing-item?)

; engines.item-viewer.routes.subs
(def get-item-route routes.subs/get-item-route)

; engines.item-viewer.update.subs
(def get-deleted-item-id   update.subs/get-deleted-item-id)
(def get-copy-item-id      update.subs/get-copy-item-id)
(def get-recovered-item-id update.subs/get-recovered-item-id)
