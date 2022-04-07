
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.items.views
    (:require [plugins.item-lister.items.views :as plugins.item-lister.items.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.items.views
(def list-item      plugins.item-lister.items.views/list-item)
(def card-item      plugins.item-lister.items.views/card-item)
(def thumbnail-item plugins.item-lister.items.views/thumbnail-item)
