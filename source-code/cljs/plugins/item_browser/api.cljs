
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.api
    (:require [pathom.api]
              [plugins.item-browser.backup.events]
              [plugins.item-browser.backup.subs]
              [plugins.item-browser.body.effects]
              [plugins.item-browser.body.events]
              [plugins.item-browser.body.subs]
              [plugins.item-browser.core.effects]
              [plugins.item-browser.download.effects]
              [plugins.item-browser.download.events]
              [plugins.item-browser.download.subs]
              [plugins.item-browser.items.events]
              [plugins.item-browser.routes.effects]
              [plugins.item-browser.transfer.subs]
              [plugins.item-browser.update.effects]
              [plugins.item-browser.update.events]
              [plugins.item-browser.update.subs]
              [plugins.item-browser.body.views       :as body.views]
              [plugins.item-lister.core.events       :as core.events]
              [plugins.item-browser.core.subs        :as core.subs]
              [plugins.item-browser.items.subs       :as items.subs]
              [plugins.item-browser.routes.subs      :as routes.subs]
              [plugins.item-browser.selection.events :as selection.events]
              [plugins.item-browser.selection.subs   :as selection.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-browser.body.views
(def body body.views/body)

; plugins.item-browser.core.events
(def set-meta-item! core.events/set-meta-item!)

; plugins.item-browser.core.subs
(def get-meta-item       core.subs/get-meta-item)
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
(def browsing-item?      core.subs/browsing-item?)

; plugins.item-browser.items.subs
(def get-item items.subs/get-item)

; plugins.item-browser.routes.subs
(def get-item-route routes.subs/get-item-route)

; plugins.item-browser.selection.events
(def toggle-item-selection!         selection.events/toggle-item-selection!)
(def toggle-single-item-selection!  selection.events/toggle-single-item-selection!)
(def toggle-limited-item-selection! selection.events/toggle-limited-item-selection!)
(def import-selection!              selection.events/import-selection!)
(def import-single-selection!       selection.events/import-single-selection!)

; plugins.item-browser.selection.subs
(def get-selected-item-count selection.subs/get-selected-item-count)
(def item-selected?          selection.subs/item-selected?)
(def export-selection        selection.subs/export-selection)
(def export-single-selection selection.subs/export-single-selection)
