
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.items.views
    (:require [plugins.item-lister.items.views :as items.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.items.views
(def list-item      items.views/list-item)
(def card-item      items.views/card-item)
(def thumbnail-item items.views/thumbnail-item)
