
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.subs
    (:require [plugins.item-lister.download.subs    :as plugins.item-lister.download.subs]
              [plugins.plugin-handler.download.subs :as download.subs]
              [x.app-core.api                       :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.download.subs
(def first-data-received? plugins.item-lister.download.subs/first-data-received?)

; plugins.plugin-handler.download.subs
(def get-resolver-id download.subs/get-resolver-id)
(def data-received?  download.subs/data-received?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:item-browser/data-received? :my-browser]
(a/reg-sub :item-browser/data-received? data-received?)

; @usage
;  [:item-browser/first-data-received? :my-browser]
(a/reg-sub :item-browser/first-data-received? first-data-received?)
