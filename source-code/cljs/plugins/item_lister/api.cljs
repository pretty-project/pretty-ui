
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.api
    (:require [pathom.api]
              [plugins.item-lister.backup.events]
              [plugins.item-lister.backup.subs]
              [plugins.item-lister.body.effects]
              [plugins.item-lister.body.events]
              [plugins.item-lister.body.subs]
              [plugins.item-lister.core.effects]
              [plugins.item-lister.core.events]
              [plugins.item-lister.core.subs]
              [plugins.item-lister.download.effects]
              [plugins.item-lister.download.events]
              [plugins.item-lister.download.subs]
              [plugins.item-lister.routes.effects]
              [plugins.item-lister.transfer.subs]
              [plugins.item-lister.update.effects]
              [plugins.item-lister.update.events]
              [plugins.item-lister.update.subs]
              [plugins.item-lister.body.views   :as body.views]
              [plugins.item-lister.items.events :as items.events]
              [plugins.item-lister.items.subs   :as items.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.body.views
(def body body.views/body)

; plugins.item-lister.items.events
(def toggle-item-selection! items.events/toggle-item-selection!)

; plugins.item-lister.items.subs
(def get-selected-item-ids  items.subs/get-selected-item-ids)
(def toggle-item-selection? items.subs/toggle-item-selection?)
