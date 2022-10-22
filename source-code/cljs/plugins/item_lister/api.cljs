
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
              [plugins.item-lister.download.effects]
              [plugins.item-lister.download.events]
              [plugins.item-lister.download.subs]
              [plugins.item-lister.items.events]
              [plugins.item-lister.routes.effects]
              [plugins.item-lister.transfer.subs]
              [plugins.item-lister.update.effects]
              [plugins.item-lister.update.events]
              [plugins.item-lister.update.subs]
              [plugins.item-lister.body.views       :as body.views]
              [plugins.item-lister.core.events      :as core.events]
              [plugins.item-lister.core.subs        :as core.subs]
              [plugins.item-lister.items.subs       :as items.subs]
              [plugins.item-lister.selection.events :as selection.events]
              [plugins.item-lister.selection.subs   :as selection.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.body.views
(def body body.views/body)

; plugins.item-lister.core.events
(def set-meta-item! core.events/set-meta-item!)

; plugins.item-lister.core.subs
(def get-meta-item           core.subs/get-meta-item)
(def get-downloaded-items    core.subs/get-downloaded-items)
(def export-downloaded-items core.subs/export-downloaded-items)

; plugins.item-lister.items.subs
(def get-item items.subs/get-item)

; plugins.item-lister.selection.events
(def toggle-item-selection!         selection.events/toggle-item-selection!)
(def toggle-single-item-selection!  selection.events/toggle-single-item-selection!)
(def toggle-limited-item-selection! selection.events/toggle-limited-item-selection!)
(def import-selection!              selection.events/import-selection!)
(def import-single-selection!       selection.events/import-single-selection!)

; plugins.item-lister.selection.subs
(def get-selected-item-count selection.subs/get-selected-item-count)
(def item-selected?          selection.subs/item-selected?)
(def export-selection        selection.subs/export-selection)
(def export-single-selection selection.subs/export-single-selection)
