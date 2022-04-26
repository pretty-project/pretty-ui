
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.subs
    (:require [plugins.plugin-handler.download.subs :as download.subs]
              [x.app-core.api                       :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.download.subs
(def get-resolver-id download.subs/get-resolver-id)
(def data-received?  download.subs/data-received?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:item-browser/data-received? :my-browser]
(a/reg-sub :item-browser/data-received? data-received?)
