
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.items.subs
    (:require [engines.engine-handler.items.subs :as items.subs]
              [re-frame.api                      :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.items.subs
(def item-changed?         items.subs/item-changed?)
(def current-item-changed? items.subs/current-item-changed?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
; [:item-handler/current-item-changed? :my-handler]
(r/reg-sub :item-handler/current-item-changed? current-item-changed?)
