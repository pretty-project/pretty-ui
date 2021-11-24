
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.2.6
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.api
    (:require [app-plugins.item-lister.engine :as engine]
              [app-plugins.item-lister.views  :as views]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  More info: server-plugins.item-lister.api
;
; @usage
;  (ns my-namespace (:require [app-plugins.item-lister.api :item-lister :refer [item-lister]]))
;
;  (defn list-item [item-dex item] ...)
;  (defn view      [_] [item-lister :my-extension :my-type {:list-item #'list-item}])
;  (a/reg-event-fx :my-extension/render-my-type-lister! [:ui/set-surface! {:content #'view}])
;
;  (a/dispatch [:router/go-to! "/my-extension"])



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.item-lister.engine
(def DEFAULT-ORDER-BY          engine/DEFAULT-ORDER-BY)
(def DEFAULT-ORDER-BY-OPTIONS  engine/DEFAULT-ORDER-BY-OPTIONS)
(def request-id                engine/request-id)
(def resolver-id               engine/resolver-id)
(def new-item-uri              engine/new-item-uri)
(def synchronizing?            engine/synchronizing?)
(def get-downloaded-items      engine/get-downloaded-items)
(def get-downloaded-item-count engine/get-downloaded-item-count)
(def get-all-item-count        engine/get-all-item-count)
(def get-header-props          engine/get-header-props)
(def get-view-props            engine/get-view-props)
(def toggle-search-mode!       engine/toggle-search-mode!)
(def toggle-select-mode!       engine/toggle-select-mode!)

; app-plugins.item-lister.views
(def quit-search-mode-button      views/quit-search-mode-button)
(def search-mode-button           views/search-mode-button)
(def search-items-field           views/search-items-field)
(def search-header                views/search-header)
(def new-item-button              views/new-item-button)
(def new-item-select              views/new-item-select)
(def select-multiple-items-button views/select-multiple-items-button)
(def delete-selected-items-button views/delete-selected-items-button)
(def toggle-item-filter-visibility-button views/toggle-item-filter-visibility-button)
(def sort-items-button            views/sort-items-button)
(def item-lister                  views/view)
