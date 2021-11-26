
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.4.0
; Compatibility: x4.1.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.partition-handler
    (:require [x.app-core.api             :as a]
              [x.mid-db.partition-handler :as partition-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.partition-handler
(def data-item-path                 partition-handler/data-item-path)
(def path                           partition-handler/path)
(def data-item-cofx-path            partition-handler/data-item-cofx-path)
(def cofx-path                      partition-handler/cofx-path)
(def meta-item-path                 partition-handler/meta-item-path)
(def meta-item-cofx-path            partition-handler/meta-item-cofx-path)
(def data-index-path                partition-handler/data-index-path)
(def data-index-cofx-path           partition-handler/data-index-cofx-path)
(def data-history-path              partition-handler/data-history-path)
(def data-history-cofx-path         partition-handler/data-history-cofx-path)
(def partition->data-items          partition-handler/partition->data-items)
(def partition->data-item           partition-handler/partition->data-item)
(def partition->data-order          partition-handler/partition->data-order)
(def partition->partition-ordered?  partition-handler/partition->partition-ordered?)
(def partition->meta-items          partition-handler/partition->meta-items)
(def partition->meta-item           partition-handler/partition->meta-item)
(def partition->partition-empty?    partition-handler/partition->partition-empty?)
(def partition->partition-nonempty? partition-handler/partition->partition-nonempty?)
(def get-partition                  partition-handler/get-partition)
(def partition-exists?              partition-handler/partition-exists?)
(def get-data-items                 partition-handler/get-data-items)
(def get-data-item                  partition-handler/get-data-item)
(def data-item-exists?              partition-handler/data-item-exists?)
(def get-filtered-data-items        partition-handler/get-filtered-data-items)
(def get-data-item-count            partition-handler/get-data-item-count)
(def get-meta-items                 partition-handler/get-meta-items)
(def get-meta-item                  partition-handler/get-meta-item)
(def get-data-order                 partition-handler/get-data-order)
(def partition-ordered?             partition-handler/partition-ordered?)
(def get-data-index                 partition-handler/get-data-index)
(def partition-indexed?             partition-handler/partition-indexed?)
(def partition-empty?               partition-handler/partition-empty?)
(def reg-partition!                 partition-handler/reg-partition!)
(def remove-partition!              partition-handler/remove-partition!)
(def merge-partitions!              partition-handler/merge-partitions!)
(def add-data-items!                partition-handler/add-data-items!)
(def set-meta-item!                 partition-handler/set-meta-item!)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:db/get-partition :my-partition/primary]
(a/reg-sub :db/get-partition get-partition)

; @usage
;  [:db/partition-empty? :my-partition/primary]
(a/reg-sub :db/partition-empty? partition-empty?)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  ...
(a/reg-event-db :db/reg-partition! reg-partition!)

; @usage
;  [:db/remove-partition! [:partition :path]]
(a/reg-event-db :db/remove-partition! remove-partition!)
