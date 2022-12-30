
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.items.events
    (:require [engines.engine-handler.items.events :as items.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.items.events
(def mark-item-as-changed!   items.events/mark-item-as-changed!)
(def unmark-item-as-changed! items.events/unmark-item-as-changed!)
