
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-db.core.subs
    (:require [mid.x.db.core.subs :as core.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.db.core.subs
(def subscribe-item   core.subs/subscribe-item)
(def subscribed-item  core.subs/subscribed-item)
(def get-db           core.subs/get-db)
(def get-item         core.subs/get-item)
(def item-exists?     core.subs/item-exists?)
(def get-item-count   core.subs/get-item-count)
(def get-applied-item core.subs/get-applied-item)
