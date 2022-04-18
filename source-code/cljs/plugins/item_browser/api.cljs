
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.api
    (:require [plugins.item-browser.backup.events]
              [plugins.item-browser.backup.subs]
              [plugins.item-browser.core.effects]
              [plugins.item-browser.core.events]
              [plugins.item-browser.download.effects]
              [plugins.item-browser.download.events]
              [plugins.item-browser.download.subs]
              [plugins.item-browser.mount.effects]
              [plugins.item-browser.mount.events]
              [plugins.item-browser.mount.subs]
              [plugins.item-browser.routes.effects]
              [plugins.item-browser.routes.events]
              [plugins.item-browser.update.effects]
              [plugins.item-browser.update.events]
              [plugins.item-browser.update.subs]
              [plugins.item-browser.body.views   :as body.views]
              [plugins.item-browser.core.subs    :as core.subs]
              [plugins.item-browser.header.views :as header.views]
              [plugins.item-browser.items.events :as items.events]
              [plugins.item-browser.items.subs   :as items.subs]
              [plugins.item-browser.items.views  :as items.views]
              [plugins.item-browser.routes.subs  :as routes.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-browser.body.views
(def body body.views/body)

; plugins.item-browser.core.subs
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
(def browsing-item?      core.subs/browsing-item?)

; plugins.item-browser.header.views
(def go-home-icon-button header.views/go-home-icon-button)
(def go-up-icon-button   header.views/go-up-icon-button)
(def header              header.views/header)

; plugins.item-browser.items.events
(def toggle-item-selection! items.events/toggle-item-selection!)

; plugins.item-browser.items.subs
(def toggle-item-selection? items.subs/toggle-item-selection?)

; plugins.item-lister.items.views
(def list-item      items.views/list-item)
(def card-item      items.views/card-item)
(def thumbnail-item items.views/thumbnail-item)

; plugins.item-browser.routes.subs
(def get-item-route routes.subs/get-item-route)
