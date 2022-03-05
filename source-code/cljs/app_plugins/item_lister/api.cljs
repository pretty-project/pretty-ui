
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.api
    (:require [app-plugins.item-lister.dialogs]
              [app-plugins.item-lister.effects]
              [app-plugins.item-lister.queries]
              [app-plugins.item-lister.engine :as engine]
              [app-plugins.item-lister.events :as events]
              [app-plugins.item-lister.subs   :as subs]
              [app-plugins.item-lister.views  :as views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.item-lister.engine
(def request-id engine/request-id)

; app-plugins.item-lister.events
(def toggle-item-selection! events/toggle-item-selection!)

; app-plugins.item-lister.subs
(def toggle-item-selection? subs/toggle-item-selection?)

; app-plugins.item-lister.views
(def search-block               views/search-block)
(def new-item-button            views/new-item-button)
(def new-item-select            views/new-item-select)
(def toggle-select-mode-button  views/toggle-select-mode-button)
(def toggle-reorder-mode-button views/toggle-reorder-mode-button)
(def sort-items-button          views/sort-items-button)
(def header                     views/header)
(def body                       views/body)
