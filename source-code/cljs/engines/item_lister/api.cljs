
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.api
    (:require [engines.item-lister.backup.events]
              [engines.item-lister.backup.subs]
              [engines.item-lister.body.effects]
              [engines.item-lister.body.events]
              [engines.item-lister.core.effects]
              [engines.item-lister.download.effects]
              [engines.item-lister.download.events]
              [engines.item-lister.transfer.subs]
              [engines.item-lister.update.effects]
              [engines.item-lister.update.events]
              [engines.item-lister.update.subs]
              [engines.item-lister.body.subs        :as body.subs]
              [engines.item-lister.body.views       :as body.views]
              [engines.item-lister.core.events      :as core.events]
              [engines.item-lister.core.subs        :as core.subs]
              [engines.item-lister.download.subs    :as download.subs]
              [engines.item-lister.items.events     :as items.events]
              [engines.item-lister.items.subs       :as items.subs]
              [engines.item-lister.selection.events :as selection.events]
              [engines.item-lister.selection.subs   :as selection.subs]
              [engines.item-lister.update.subs      :as update.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-lister.body.subs
(def body-did-mount? body.subs/body-did-mount?)

; engines.item-lister.body.views
(def downloader body.views/body)

; engines.item-lister.core.events
(def set-meta-item! core.events/set-meta-item!)

; engines.item-lister.core.subs
(def get-meta-item       core.subs/get-meta-item)
(def get-downloaded-item core.subs/get-downloaded-item)
(def get-item-order      core.subs/get-item-order)
(def get-listed-items    core.subs/get-listed-items)
(def export-listed-items core.subs/export-listed-items)
(def item-listed?        core.subs/item-listed?)

; engines.item-lister.download.subs
(def data-received?       download.subs/data-received?)
(def first-data-received? download.subs/first-data-received?)

; engines.item-lister.items.events
(def disable-items!    items.events/disable-items!)
(def enable-items!     items.events/enable-items!)
(def enable-all-items! items.events/enable-all-items!)

; engines.item-lister.items.subs
(def item-disabled? items.subs/item-disabled?)

; engines.item-lister.selection.events
(def select-all-items!              selection.events/select-all-items!)
(def select-item!                   selection.events/select-item!)
(def toggle-item-selection!         selection.events/toggle-item-selection!)
(def toggle-single-item-selection!  selection.events/toggle-single-item-selection!)
(def toggle-limited-item-selection! selection.events/toggle-limited-item-selection!)
(def discard-selection!             selection.events/discard-selection!)
(def disable-selected-items!        selection.events/disable-selected-items!)
(def import-selection!              selection.events/import-selection!)
(def import-single-selection!       selection.events/import-single-selection!)
(def import-limited-selection!      selection.events/import-limited-selection!)

; engines.item-lister.selection.subs
(def export-selection           selection.subs/export-selection)
(def export-single-selection    selection.subs/export-single-selection)
(def get-selected-item-count    selection.subs/get-selected-item-count)
(def all-listed-items-selected? selection.subs/all-listed-items-selected?)
(def any-item-selected?         selection.subs/any-item-selected?)
(def any-listed-item-selected?  selection.subs/any-listed-item-selected?)
(def no-items-selected?         selection.subs/no-items-selected?)
(def item-selected?             selection.subs/item-selected?)

; engines.item-lister.update.subs
(def get-deleted-item-ids    update.subs/get-deleted-item-ids)
(def get-duplicated-item-ids update.subs/get-duplicated-item-ids)
