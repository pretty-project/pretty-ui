
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.download.subs
    (:require [engines.item-lister.download.subs :as download.subs]
              [re-frame.api                      :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-lister.download.subs
(def get-resolver-id      download.subs/get-resolver-id)
(def get-resolver-answer  download.subs/get-resolver-answer)
(def data-received?       download.subs/data-received?)
(def first-data-received? download.subs/first-data-received?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
; [:item-browser/data-received? :my-browser]
(r/reg-sub :item-browser/data-received? data-received?)

; @usage
; [:item-browser/first-data-received? :my-browser]
(r/reg-sub :item-browser/first-data-received? first-data-received?)
