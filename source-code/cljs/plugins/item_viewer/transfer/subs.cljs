
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.transfer.subs
    (:require [plugins.plugin-handler.transfer.subs :as transfer.subs]
              [x.app-core.api                       :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.transfer.subs
(def get-transfer-item transfer.subs/get-transfer-item)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-viewer/get-transfer-item get-transfer-item)
