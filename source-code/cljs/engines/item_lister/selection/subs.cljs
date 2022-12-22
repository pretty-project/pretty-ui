
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.selection.subs
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

; @param (keyword) lister-id
;
; @usage
; [:item-lister/get-selected-item-count :my-lister]
(r/reg-sub :item-lister/get-selected-item-count get-selected-item-count)

; @param (keyword) lister-id
;
; @usage
; [:item-lister/all-listed-items-selected? :my-lister]
(r/reg-sub :item-lister/all-listed-items-selected? all-listed-items-selected?)

; @param (keyword) lister-id
;
; @usage
; [:item-lister/any-item-selected? :my-lister]
(r/reg-sub :item-lister/any-item-selected? any-item-selected?)

; @param (keyword) lister-id
;
; @usage
; [:item-lister/any-listed-item-selected? :my-lister]
(r/reg-sub :item-lister/any-listed-item-selected? any-listed-item-selected?)

; @param (keyword) lister-id
;
; @usage
; [:item-lister/no-items-selected? :my-lister]
(r/reg-sub :item-lister/no-items-selected? no-items-selected?)

; @param (keyword) lister-id
; @param (string) item-id
;
; @usage
; [:item-lister/item-selected? :my-lister "my-item"]
(r/reg-sub :item-lister/item-selected? item-selected?)
