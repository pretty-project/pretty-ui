
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.api
    (:require [pathom.api]
              [engines.item-browser.backup.events]
              [engines.item-browser.backup.subs]
              [engines.item-browser.body.effects]
              [engines.item-browser.body.events]
              [engines.item-browser.body.subs]
              [engines.item-browser.core.effects]
              [engines.item-browser.download.effects]
              [engines.item-browser.download.events]
              [engines.item-browser.download.subs]
              [engines.item-browser.items.events]
              [engines.item-browser.routes.effects]
              [engines.item-browser.transfer.subs]
              [engines.item-browser.update.effects]
              [engines.item-browser.update.events]
              [engines.item-browser.update.subs]
              [engines.item-browser.body.views       :as body.views]
              [engines.item-browser.core.events      :as core.events]
              [engines.item-browser.core.subs        :as core.subs]
              [engines.item-browser.items.subs       :as items.subs]
              [engines.item-browser.routes.subs      :as routes.subs]
              [engines.item-browser.selection.events :as selection.events]
              [engines.item-browser.selection.subs   :as selection.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-browser.body.views
(def body body.views/body)

; engines.item-browser.core.events
(def set-meta-item! core.events/set-meta-item!)
(def set-items!     core.events/set-items!)

; engines.item-browser.core.subs
(def get-meta-item       core.subs/get-meta-item)
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
(def browsing-item?      core.subs/browsing-item?)

; engines.item-browser.items.subs
(def get-item items.subs/get-item)

; engines.item-browser.routes.subs
(def get-item-route routes.subs/get-item-route)

; engines.item-browser.selection.events
(def toggle-item-selection!         selection.events/toggle-item-selection!)
(def toggle-single-item-selection!  selection.events/toggle-single-item-selection!)
(def toggle-limited-item-selection! selection.events/toggle-limited-item-selection!)
(def import-selection!              selection.events/import-selection!)
(def import-single-selection!       selection.events/import-single-selection!)

; engines.item-browser.selection.subs
(def get-selected-item-count selection.subs/get-selected-item-count)
(def item-selected?          selection.subs/item-selected?)
(def export-selection        selection.subs/export-selection)
(def export-single-selection selection.subs/export-single-selection)
