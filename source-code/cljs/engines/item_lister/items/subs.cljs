
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.items.subs
    (:require [engines.engine-handler.items.subs :as items.subs]
              [re-frame.api                      :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.items.subs
(def item-disabled? items.subs/item-disabled?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) lister-id
; @param (string) item-id
;
; @usage
; [:item-lister/item-disabled? :my-lister "my-item"]
(r/reg-sub :item-lister/item-disabled? item-disabled?)
