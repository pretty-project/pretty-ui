
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.items.subs
    (:require [engines.engine-handler.items.subs :as items.subs]
              [re-frame.api                      :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.items.subs
(def item-disabled? items.subs/item-disabled?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) browser-id
; @param (string) item-id
;
; @usage
; [:item-browser/item-disabled? :my-browser "my-item"]
(r/reg-sub :item-browser/item-disabled? item-disabled?)
