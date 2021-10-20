
(ns plugins.item-lister.api
   (:require [plugins.item-lister.items  :as items]
             [plugins.item-lister.header :as header]))



;; ----------------------------------------------------------------------------
;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.core
(def items items/view)

; plugins.item-lister.header
(def search-bar header/search-bar)
