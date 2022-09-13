
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
              [plugins.item-browser.core.events]
              [plugins.item-browser.download.effects]
              [plugins.item-browser.download.events]
              [plugins.item-browser.download.subs]
              [plugins.item-browser.routes.effects]
              [plugins.item-browser.transfer.subs]
              [plugins.item-browser.update.effects]
              [plugins.item-browser.update.events]
              [plugins.item-browser.update.subs]
              [plugins.item-browser.body.views   :as body.views]
              [plugins.item-browser.core.subs    :as core.subs]
              [plugins.item-browser.items.events :as items.events]
              [plugins.item-browser.items.subs   :as items.subs]
              [plugins.item-browser.routes.subs  :as routes.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-browser.body.views
(def body body.views/body)

; plugins.item-browser.core.subs
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
(def browsing-item?      core.subs/browsing-item?)

; plugins.item-browser.items.events
(def toggle-item-selection! items.events/toggle-item-selection!)

; plugins.item-browser.items.subs
(def get-selected-item-ids items.subs/get-selected-item-ids)

; plugins.item-browser.routes.subs
(def get-item-route routes.subs/get-item-route)
