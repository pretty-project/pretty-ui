
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.24
; Description:
; Version: v0.6.8
; Compatibility: x4.1.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-db.engine
    (:require [mid-fruits.candy  :refer [return]]
              [x.mid-db.engine   :as engine]
              [x.server-core.api :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.engine
(def item-path->cofx-path engine/item-path->cofx-path)
(def get-db               engine/get-db)
(def get-item             engine/get-item)
(def item-exists?         engine/item-exists?)
(def empty-db!            engine/empty-db!)
(def move-item!           engine/move-item!)
(def set-item!            engine/set-item!)
(def set-vector-item!     engine/set-vector-item!)
(def remove-item!         engine/remove-item!)
(def remove-vector-item!  engine/remove-vector-item!)
(def remove-item-n!       engine/remove-item-n!)
(def inc-item-n!          engine/inc-item-n!)
(def dec-item-n!          engine/dec-item-n!)
(def apply!               engine/apply!)
(def distribute-items!    engine/distribute-items!)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-db
  [db _]
  (return db))

(a/reg-sub :x.server-db/get-db get-db)
