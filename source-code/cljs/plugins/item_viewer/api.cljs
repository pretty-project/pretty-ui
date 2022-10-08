
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.api
    (:require [pathom.api]
              [plugins.item-viewer.backup.events]
              [plugins.item-viewer.backup.subs]
              [plugins.item-viewer.body.effects]
              [plugins.item-viewer.body.events]
              [plugins.item-viewer.body.subs]
              [plugins.item-viewer.core.effects]
              [plugins.item-viewer.core.events]
              [plugins.item-viewer.download.effects]
              [plugins.item-viewer.download.events]
              [plugins.item-viewer.download.subs]
              [plugins.item-viewer.routes.effects]
              [plugins.item-viewer.transfer.subs]
              [plugins.item-viewer.update.effects]
              [plugins.item-viewer.update.events]
              [plugins.item-viewer.update.subs]
              [plugins.item-viewer.body.views   :as body.views]
              [plugins.item-viewer.core.subs    :as core.subs]
              [plugins.item-viewer.routes.subs  :as routes.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-viewer.body.views
(def body body.views/body)

; plugins.item-viewer.core.subs
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
(def export-current-item core.subs/export-current-item)
(def viewing-item?       core.subs/viewing-item?)

; plugins.item-viewer.routes.subs
(def get-item-route routes.subs/get-item-route)
