
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.api
    (:require [engines.item-browser.backup.events]
              [engines.item-browser.backup.subs]
              [engines.item-browser.body.effects]
              [engines.item-browser.body.events]
              [engines.item-browser.core.effects]
              [engines.item-browser.download.effects]
              [engines.item-browser.download.events]
              [engines.item-browser.transfer.subs]
              [engines.item-browser.update.effects]
              [engines.item-browser.update.events]
              [engines.item-browser.update.subs]
              [engines.item-browser.body.subs        :as body.subs]
              [engines.item-browser.body.views       :as body.views]
              [engines.item-browser.core.events      :as core.events]
              [engines.item-browser.core.subs        :as core.subs]
              [engines.item-browser.download.subs    :as download.subs]
              [engines.item-browser.items.events     :as items.events]
              [engines.item-browser.items.subs       :as items.subs]
              [engines.item-browser.selection.events :as selection.events]
              [engines.item-browser.selection.subs   :as selection.subs]
              [engines.item-browser.update.subs      :as update.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-browser.body.subs
(def body-did-mount? body.subs/body-did-mount?)

; engines.item-browser.body.views
(def body body.views/body)

; engines.item-browser.core.events
(def set-meta-item! core.events/set-meta-item!)
(def set-item-id!   core.events/set-item-id!)

; engines.item-browser.core.subs
(def get-meta-item       core.subs/get-meta-item)
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
(def get-default-item-id core.subs/get-default-item-id)
(def get-parent-item-id  core.subs/get-parent-item-id)
(def browsing-item?      core.subs/browsing-item?)
(def get-item-order      core.subs/get-item-order)
(def item-listed?        core.subs/item-listed?)

; engines.item-browser.download.subs
(def data-received?       download.subs/data-received?)
(def first-data-received? download.subs/first-data-received?)

; engines.item-browser.items.events
(def disable-items!    items.events/disable-items!)
(def enable-items!     items.events/enable-items!)
(def enable-all-items! items.events/enable-all-items!)
(def disable-item!     items.events/disable-item!)
(def enable-item!      items.events/enable-item!)

; engines.item-browser.items.subs
(def item-disabled? items.subs/item-disabled?)

; engines.item-browser.selection.events
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

; engines.item-browser.selection.subs
(def export-selection           selection.subs/export-selection)
(def export-single-selection    selection.subs/export-single-selection)
(def get-selected-item-count    selection.subs/get-selected-item-count)
(def all-listed-items-selected? selection.subs/all-listed-items-selected?)
(def any-item-selected?         selection.subs/any-item-selected?)
(def any-listed-item-selected?  selection.subs/any-listed-item-selected?)
(def no-items-selected?         selection.subs/no-items-selected?)
(def item-selected?             selection.subs/item-selected?)

; engines.item-browser.update.subs
(def get-deleted-item-id   update.subs/get-deleted-item-id)
(def get-copy-item-id      update.subs/get-copy-item-id)
(def get-recovered-item-id update.subs/get-recovered-item-id)
(def parent-item-browsed?  update.subs/parent-item-browsed?)
