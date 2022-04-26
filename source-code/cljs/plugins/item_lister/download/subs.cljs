
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.download.subs
    (:require [plugins.plugin-handler.download.subs :as download.subs]
              [x.app-core.api                       :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.download.subs
(def get-resolver-id download.subs/get-resolver-id)
(def data-received?  download.subs/data-received?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/data-received? :my-lister]
(a/reg-sub :item-lister/data-received? data-received?)
