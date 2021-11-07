
(ns plugins.item-lister.api
    (:require [plugins.item-lister.engine :as engine]
              [plugins.item-lister.views  :as views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.engine
(def DEFAULT-ORDER-BY          engine/DEFAULT-ORDER-BY)
(def DEFAULT-ORDER-BY-OPTIONS  engine/DEFAULT-ORDER-BY-OPTIONS)
(def synchronizing?            engine/synchronizing?)
(def get-downloaded-items      engine/get-downloaded-items)
(def get-downloaded-item-count engine/get-downloaded-item-count)
(def get-all-item-count        engine/get-all-item-count)
(def get-header-view-props     engine/get-header-view-props)
(def get-view-props            engine/get-view-props)
(def toggle-search-mode!       engine/toggle-search-mode!)
(def toggle-select-mode!       engine/toggle-select-mode!)

; plugins.item-lister.views
(def quit-search-mode-button      views/quit-search-mode-button)
(def search-mode-button           views/search-mode-button)
(def search-items-field           views/search-items-field)
(def search-header                views/search-header)
(def new-item-button              views/new-item-button)
(def new-item-select              views/new-item-select)
(def select-multiple-items-button views/select-multiple-items-button)
(def delete-selected-items-button views/delete-selected-items-button)
(def sort-items-button            views/sort-items-button)
(def item-lister                  views/view)
