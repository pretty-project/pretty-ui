
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.transfer.subs
    (:require [plugins.plugin-handler.transfer.subs :as transfer.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.transfer.subs
(def get-transfer-item transfer.subs/get-transfer-item)
