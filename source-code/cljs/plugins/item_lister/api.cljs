
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
              [plugins.item-lister.update.effects]
              [plugins.item-lister.update.events]
              [plugins.item-lister.update.subs]
              [plugins.item-lister.core.views  :as core.views]
              [plugins.item-lister.items.events :as items.events]
              [plugins.item-lister.items.subs   :as items.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.items.events
(def toggle-item-selection! items.events/toggle-item-selection!)

; plugins.item-lister.items.subs
(def toggle-item-selection? items.subs/toggle-item-selection?)

; plugins.item-lister.core.views
(def search-block               core.views/search-block)
(def new-item-button            core.views/new-item-button)
(def toggle-select-mode-button  core.views/toggle-select-mode-button)
(def toggle-reorder-mode-button core.views/toggle-reorder-mode-button)
(def sort-items-button          core.views/sort-items-button)
(def header                     core.views/header)
(def body                       core.views/body)
