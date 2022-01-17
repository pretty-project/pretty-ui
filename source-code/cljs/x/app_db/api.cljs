
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.20
; Description:
; Version: v0.7.0
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.api
    (:require [x.app-db.backup-handler       :as backup-handler]
              [x.app-db.collection-handler   :as collection-handler]
              [x.app-db.data-history-handler :as data-history-handler]
              [x.app-db.data-order-handler   :as data-order-handler]
              [x.app-db.data-range-handler   :as data-range-handler]
              [x.app-db.document-handler     :as document-handler]
              [x.app-db.engine               :as engine]
              [x.app-db.id-handler           :as id-handler]
              [x.app-db.partition-handler    :as partition-handler]
              [x.app-db.partition-state      :as partition-state]))



;; -- XXX#4009 ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A) Item
;
; Item megnevezéssel az Re-Frame adatbázis bármely elemére lehet hivatkozni.
; Az item elérési útvonala az adatbázisban az item-path.
;
; @example
;  [:my-item :my-subitem]


; B) Data-item
;
; Data-item megnevezéssel a partíciók :data-items térképében tárolt elemeire
; lehet hivatkozni.
; A data-item elérési útvonala az adatbázisban a data-item-path
;
; @example
;  [::my-partition :data-items :my-data-item :my-data-subitem]



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-db.backup-handler
(def item-path->backup-item-path backup-handler/item-path->backup-item-path)
(def get-backup-item             backup-handler/get-backup-item)
(def item-changed?               backup-handler/item-changed?)
(def item-unchanged?             backup-handler/item-unchanged?)
(def store-backup-item!          backup-handler/store-backup-item!)
(def restore-backup-item!        backup-handler/restore-backup-item!)
(def remove-backup-item!         backup-handler/remove-backup-item!)

; x.app-db.collection-handler
(def collection->namespace                  collection-handler/collection->namespace)
(def collection->namespaced-collection      collection-handler/collection->namespaced-collection)
(def collection->non-namespaced-collection  collection-handler/collection->non-namespaced-collection)
(def trim-collection                        collection-handler/trim-collection)
(def sort-collection                        collection-handler/sort-collection)
(def filter-documents                       collection-handler/filter-documents)
(def filter-document                        collection-handler/filter-document)
(def match-documents                        collection-handler/match-documents)
(def match-document                         collection-handler/match-document)
(def get-documents-kv                       collection-handler/get-documents-kv)
(def get-document-kv                        collection-handler/get-document-kv)
(def get-document                           collection-handler/get-document)
(def get-document-item                      collection-handler/get-document-item)
(def get-documents                          collection-handler/get-documents)
(def add-document                           collection-handler/add-document)
(def remove-document                        collection-handler/remove-document)
(def remove-documents                       collection-handler/remove-documents)
(def update-document                        collection-handler/update-document)
(def update-document-item                   collection-handler/update-document-item)
(def document-exists?                       collection-handler/document-exists?)
(def explode-collection                     collection-handler/explode-collection)
(def get-specified-values                   collection-handler/get-specified-values)

; x.app-db.data-history-handler
(def get-partition-history      data-history-handler/get-partition-history)
(def get-data-history           data-history-handler/get-data-history)
(def get-last-data-history-item data-history-handler/get-last-data-history-item)
(def get-data-history-result    data-history-handler/get-data-history-result)
(def clear-data-history!        data-history-handler/clear-data-history!)
(def update-data-history!       data-history-handler/update-data-history!)

; x.app-db.data-order-handler
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
(def remove-data-item-id!     data-order-handler/remove-data-item-id!)
(def remove-data-item-props!  data-order-handler/remove-data-item-props!)

; x.app-db.data-range-handler
(def data-cursor-value-in-threshold?   data-range-handler/data-cursor-value-in-threshold?)
(def get-data-cursor-high              data-range-handler/get-data-cursor-high)
(def get-data-cursor-low               data-range-handler/get-data-cursor-low)
(def data-range-passable?              data-range-handler/data-range-passable?)
(def get-first-data-item-id-in-range   data-range-handler/get-first-data-item-id-in-range)
(def get-first-data-item-in-range      data-range-handler/get-first-data-item-in-range)
(def get-last-data-item-id-in-range    data-range-handler/get-last-data-item-id-in-range)
(def get-last-data-item-in-range       data-range-handler/get-last-data-item-in-range)
(def get-first-data-item-id-post-range data-range-handler/get-first-data-item-id-post-range)
(def get-first-data-item-post-range    data-range-handler/get-first-data-item-post-range)
(def get-last-data-item-id-pre-range   data-range-handler/get-last-data-item-id-pre-range)
(def get-last-data-item-pre-range      data-range-handler/get-last-data-item-pre-range)
(def get-in-range-data-order           data-range-handler/get-in-range-data-order)
(def get-in-range-data-items           data-range-handler/get-in-range-data-items)
(def get-pre-range-data-order          data-range-handler/get-pre-range-data-order)
(def get-pre-range-data-items          data-range-handler/get-pre-range-data-items)
(def get-post-range-data-order         data-range-handler/get-post-range-data-order)
(def get-post-range-data-items         data-range-handler/get-post-range-data-items)
(def partition-ranged?                 data-range-handler/partition-ranged?)
(def step-data-cursor-high-bwd!        data-range-handler/step-data-cursor-high-bwd!)
(def step-data-cursor-high-fwd!        data-range-handler/step-data-cursor-high-fwd!)
(def step-data-cursor-low-bwd!         data-range-handler/step-data-cursor-low-bwd!)
(def step-data-cursor-low-fwd!         data-range-handler/step-data-cursor-low-fwd!)
(def trim-partition!                   data-range-handler/trim-partition!)

; x.app-db.document-handler
(def item-key->non-namespaced-item-key  document-handler/item-key->non-namespaced-item-key)
(def item-key->namespaced-item-key      document-handler/item-key->namespaced-item-key)
(def document->namespace                document-handler/document->namespace)
(def document->namespace?               document-handler/document->namespace?)
(def document->document-namespaced?     document-handler/document->document-namespaced?)
(def document->document-non-namespaced? document-handler/document->document-non-namespaced?)
(def document-contains-key?             document-handler/document-contains-key?)
(def assoc-document-value               document-handler/assoc-document-value)
(def dissoc-document-value              document-handler/dissoc-document-value)
(def get-document-value                 document-handler/get-document-value)
(def document->namespaced-document      document-handler/document->namespaced-document)
(def document->non-namespaced-document  document-handler/document->non-namespaced-document)
(def document->document-id              document-handler/document->document-id)
(def document->unidentified-document    document-handler/document->unidentified-document)
(def document->pure-document            document-handler/document->pure-document)
(def document->identified-document      document-handler/document->identified-document)
(def document->item-value               document-handler/document->item-value)
(def document->item-key                 document-handler/document->item-key)

; x.app-db.engine
(def item-path->cofx-path engine/item-path->cofx-path)
(def subscribe-item       engine/subscribe-item)
(def subscribed-item      engine/subscribed-item)
(def get-db               engine/get-db)
(def get-item             engine/get-item)
(def item-exists?         engine/item-exists?)
(def empty-db!            engine/empty-db!)
(def copy-item!           engine/copy-item!)
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

; x.app-db.id-handler
(def document-entity?             id-handler/document-entity?)
(def document-link?               id-handler/document-link?)
(def document-id->document-link   id-handler/document-id->document-link)
(def item-id->document-link       id-handler/item-id->document-link)
(def document-link->document-id   id-handler/document-link->document-id)
(def document-link->item-id       id-handler/document-link->item-id)
(def document-link->namespace     id-handler/document-link->namespace)
(def document-link->namespace?    id-handler/document-link->namespace?)
(def document-id->document-entity id-handler/document-id->document-entity)
(def item-id->document-entity     id-handler/item-id->document-entity)
(def document-entity->document-id id-handler/document-entity->document-id)
(def document-entity->item-id     id-handler/document-entity->item-id)

; x.app-db.partition-handler
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

; x.app-db.partition-state
(def partition-state->data-order          partition-state/partition-state->data-order)
(def partition-state->in-range-data-order partition-state/partition-state->in-range-data-order)
(def get-partition-state                  partition-state/get-partition-state)
