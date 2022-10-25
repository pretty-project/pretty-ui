
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.subs
    (:require [plugins.item-lister.download.subs    :as plugins.item-lister.download.subs]
              [plugins.engine-handler.download.subs :as download.subs]
              [re-frame.api                         :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.download.subs
(def first-data-received? plugins.item-lister.download.subs/first-data-received?)

; plugins.engine-handler.download.subs
(def get-resolver-id     download.subs/get-resolver-id)
(def get-resolver-answer download.subs/get-resolver-answer)
(def data-received?      download.subs/data-received?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:item-browser/data-received? :my-browser]
(r/reg-sub :item-browser/data-received? data-received?)

; @usage
;  [:item-browser/first-data-received? :my-browser]
(r/reg-sub :item-browser/first-data-received? first-data-received?)
