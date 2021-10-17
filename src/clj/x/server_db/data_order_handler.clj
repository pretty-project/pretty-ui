
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.3.8
; Compatibility: x4.1.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-db.data-order-handler
    (:require [x.mid-db.data-order-handler :as data-order-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.data-order-handler
(def data-item-last?          data-order-handler/data-item-last?)
(def data-item-first?         data-order-handler/data-item-first?)
(def get-data-item-position   data-order-handler/get-data-item-position)
(def move-data-item-to-last!  data-order-handler/move-data-item-to-last!)
(def move-data-item-to-first! data-order-handler/move-data-item-to-first!)
(def move-data-item!          data-order-handler/move-data-item!)
(def remove-data-item!        data-order-handler/remove-data-item!)
(def add-data-item!           data-order-handler/add-data-item!)
(def update-data-item!        data-order-handler/update-data-item!)
(def apply-data-item!         data-order-handler/apply-data-item!)
(def apply-data-items!        data-order-handler/apply-data-items!)
(def copy-item-to-partition!  data-order-handler/copy-item-to-partition!)
(def move-item-to-partition!  data-order-handler/move-item-to-partition!)
(def empty-partition!         data-order-handler/empty-partition!)
(def update-data-order!       data-order-handler/update-data-order!)
