
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.3.8
; Compatibility: x4.1.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-db.partition-handler
    (:require [x.mid-db.partition-handler :as partition-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.partition-handler
(def data-item-path                 partition-handler/data-item-path)
(def path                           partition-handler/path)
(def data-item-cofx-path            partition-handler/data-item-cofx-path)
(def cofx-path                      partition-handler/cofx-path)
(def meta-item-path                 partition-handler/meta-item-path)
(def meta-item-cofx-path            partition-handler/meta-item-cofx-path)
(def data-history-path              partition-handler/data-history-path)
(def data-history-cofx-path         partition-handler/data-history-cofx-path)
(def get-partition                  partition-handler/get-partition)
(def get-data-items                 partition-handler/get-data-items)
(def get-data-item                  partition-handler/get-data-item)
(def data-item-exists?              partition-handler/data-item-exists?)
(def get-data-item-count            partition-handler/get-data-item-count)
(def get-meta-items                 partition-handler/get-meta-items)
(def get-meta-item                  partition-handler/get-meta-item)
(def get-data-order                 partition-handler/get-data-order)
(def partition-ordered?             partition-handler/partition-ordered?)
(def partition-empty?               partition-handler/partition-empty?)
(def reg-partition!                 partition-handler/reg-partition!)
(def set-meta-item!                 partition-handler/set-meta-item!)
