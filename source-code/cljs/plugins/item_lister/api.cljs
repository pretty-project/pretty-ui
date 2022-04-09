
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.api
    (:require [plugins.item-lister.backup.events]
              [plugins.item-lister.backup.subs]
              [plugins.item-lister.core.effects]
              [plugins.item-lister.core.events]
              [plugins.item-lister.core.subs]
              [plugins.item-lister.download.effects]
              [plugins.item-lister.download.events]
              [plugins.item-lister.download.subs]
              [plugins.item-lister.mount.effects]
              [plugins.item-lister.mount.events]
              [plugins.item-lister.mount.subs]
              [plugins.item-lister.transfer.subs]
              [plugins.item-lister.update.effects]
              [plugins.item-lister.update.events]
              [plugins.item-lister.update.subs]
              [plugins.item-lister.body.views   :as body.views]
              [plugins.item-lister.header.views :as header.views]
              [plugins.item-lister.items.events :as items.events]
              [plugins.item-lister.items.views  :as items.views]
              [plugins.item-lister.items.subs   :as items.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.body.views
(def body body.views/body)

; plugins.item-lister.header.views
(def search-block              header.views/search-block)
(def new-item-block            header.views/new-item-block)
(def toggle-select-mode-block  header.views/toggle-select-mode-block)
(def toggle-reorder-mode-block header.views/toggle-reorder-mode-block)
(def sort-items-block          header.views/sort-items-block)
(def header                    header.views/header)

; plugins.item-lister.items.events
(def toggle-item-selection! items.events/toggle-item-selection!)

; plugins.item-lister.items.views
(def list-item      items.views/list-item)
(def card-item      items.views/card-item)
(def thumbnail-item items.views/thumbnail-item)

; plugins.item-lister.items.subs
(def toggle-item-selection? items.subs/toggle-item-selection?)
