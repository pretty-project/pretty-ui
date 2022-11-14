
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.core.events
    (:require [mid.x.db.core.events :as core.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.db.core.events
(def empty-db!           core.events/empty-db!)
(def toggle-item!        core.events/toggle-item!)
(def toggle-item-value!  core.events/toggle-item-value!)
(def copy-item!          core.events/copy-item!)
(def move-item!          core.events/move-item!)
(def set-item!           core.events/set-item!)
(def set-vector-item!    core.events/set-vector-item!)
(def remove-item!        core.events/remove-item!)
(def remove-vector-item! core.events/remove-vector-item!)
(def remove-item-n!      core.events/remove-item-n!)
(def inc-item-n!         core.events/inc-item-n!)
(def dec-item-n!         core.events/dec-item-n!)
(def apply-item!         core.events/apply-item!)
