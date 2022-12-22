
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.selection.subs
    (:require [engines.engine-handler.selection.subs :as selection.subs]
              [re-frame.api                          :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.selection.subs
(def export-selection           selection.subs/export-selection)
(def export-single-selection    selection.subs/export-single-selection)
(def get-selected-item-count    selection.subs/get-selected-item-count)
(def all-listed-items-selected? selection.subs/all-listed-items-selected?)
(def any-item-selected?         selection.subs/any-item-selected?)
(def any-listed-item-selected?  selection.subs/any-listed-item-selected?)
(def no-items-selected?         selection.subs/no-items-selected?)
(def item-selected?             selection.subs/item-selected?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) browser-id
;
; @usage
; [:item-browser/get-selected-item-count :my-browser]
(r/reg-sub :item-browser/get-selected-item-count get-selected-item-count)

; @param (keyword) browser-id
;
; @usage
; [:item-browser/all-listed-items-selected? :my-browser]
(r/reg-sub :item-browser/all-listed-items-selected? all-listed-items-selected?)

; @param (keyword) browser-id
;
; @usage
; [:item-browser/any-item-selected? :my-browser]
(r/reg-sub :item-browser/any-item-selected? any-item-selected?)

; @param (keyword) browser-id
;
; @usage
; [:item-browser/any-listed-item-selected? :my-browser]
(r/reg-sub :item-browser/any-listed-item-selected? any-listed-item-selected?)

; @param (keyword) browser-id
;
; @usage
; [:item-browser/no-items-selected? :my-browser]
(r/reg-sub :item-browser/no-items-selected? no-items-selected?)

; @param (keyword) browser-id
; @param (string) item-id
;
; @usage
; [:item-browser/item-selected? :my-browser "my-item"]
(r/reg-sub :item-browser/item-selected? item-selected?)
