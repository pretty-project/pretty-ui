
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.api
    (:require [pathom.api]
              [engines.item-lister.backup.events]
              [engines.item-lister.backup.subs]
              [engines.item-lister.body.effects]
              [engines.item-lister.body.events]
              [engines.item-lister.body.subs]
              [engines.item-lister.core.effects]
              [engines.item-lister.download.effects]
              [engines.item-lister.download.events]
              [engines.item-lister.download.subs]
              [engines.item-lister.items.events]
              [engines.item-lister.routes.effects]
              [engines.item-lister.transfer.subs]
              [engines.item-lister.update.effects]
              [engines.item-lister.update.events]
              [engines.item-lister.update.subs]
              [engines.item-lister.body.views       :as body.views]
              [engines.item-lister.core.events      :as core.events]
              [engines.item-lister.core.subs        :as core.subs]
              [engines.item-lister.items.subs       :as items.subs]
              [engines.item-lister.selection.events :as selection.events]
              [engines.item-lister.selection.subs   :as selection.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-lister.body.views
(def body body.views/body)

; engines.item-lister.core.events
(def set-meta-item! core.events/set-meta-item!)
(def set-items!     core.events/set-items!)

; engines.item-lister.core.subs
(def get-meta-item           core.subs/get-meta-item)
(def get-downloaded-items    core.subs/get-downloaded-items)
(def export-downloaded-items core.subs/export-downloaded-items)

; engines.item-lister.items.subs
(def get-item items.subs/get-item)

; engines.item-lister.selection.events
(def toggle-item-selection!         selection.events/toggle-item-selection!)
(def toggle-single-item-selection!  selection.events/toggle-single-item-selection!)
(def toggle-limited-item-selection! selection.events/toggle-limited-item-selection!)
(def import-selection!              selection.events/import-selection!)
(def import-single-selection!       selection.events/import-single-selection!)

; engines.item-lister.selection.subs
(def get-selected-item-count selection.subs/get-selected-item-count)
(def item-selected?          selection.subs/item-selected?)
(def export-selection        selection.subs/export-selection)
(def export-single-selection selection.subs/export-single-selection)
