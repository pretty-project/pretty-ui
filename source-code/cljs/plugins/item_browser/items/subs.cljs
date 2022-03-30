
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.items.subs
    (:require [plugins.item-lister.items.subs :as plugins.item-lister.items.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.items.subs
(def get-item               plugins.item-lister.items.subs/get-item)
(def get-item-dex           plugins.item-lister.items.subs/get-item-dex)
(def export-item            plugins.item-lister.items.subs/export-item)
(def toggle-item-selection? plugins.item-lister.items.subs/toggle-item-selection?)
