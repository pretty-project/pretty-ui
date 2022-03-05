
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
    (:require [x.app-db.backup-handler     :as backup-handler]
              [x.app-db.collection-handler :as collection-handler]
              [x.app-db.data-order-handler :as data-order-handler]
              [x.app-db.data-range-handler :as data-range-handler]
              [x.app-db.document-handler   :as document-handler]
              [x.app-db.engine             :as engine]
              [x.app-db.id-handler         :as id-handler]
              [x.app-db.partition-handler  :as partition-handler]))



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
(def apply-document                         collection-handler/apply-document)
(def document-exists?                       collection-handler/document-exists?)
(def explode-collection                     collection-handler/explode-collection)
(def get-specified-values                   collection-handler/get-specified-values)

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
(def document->namespace                document-handler/document->namespace)
(def document->document-namespaced?     document-handler/document->document-namespaced?)
(def assoc-document-value               document-handler/assoc-document-value)
(def dissoc-document-value              document-handler/dissoc-document-value)
(def get-document-value                 document-handler/get-document-value)
(def document->namespaced-document      document-handler/document->namespaced-document)
(def document->non-namespaced-document  document-handler/document->non-namespaced-document)
(def document->document-id              document-handler/document->document-id)
(def document->unidentified-document    document-handler/document->unidentified-document)
(def document->pure-document            document-handler/document->pure-document)
(def document->identified-document      document-handler/document->identified-document)

; x.app-db.engine
(def item-path->cofx-path engine/item-path->cofx-path)
(def subscribe-item       engine/subscribe-item)
(def subscribed-item      engine/subscribed-item)
(def get-db               engine/get-db)
(def get-item             engine/get-item)
(def item-exists?         engine/item-exists?)
(def get-item-count       engine/get-item-count)
(def get-applied-item     engine/get-applied-item)
(def empty-db!            engine/empty-db!)
(def toggle-item!         engine/toggle-item!)
(def copy-item!           engine/copy-item!)
(def move-item!           engine/move-item!)
(def set-item!            engine/set-item!)
(def set-vector-item!     engine/set-vector-item!)
(def remove-item!         engine/remove-item!)
(def remove-vector-item!  engine/remove-vector-item!)
(def remove-item-n!       engine/remove-item-n!)
(def inc-item-n!          engine/inc-item-n!)
(def dec-item-n!          engine/dec-item-n!)
(def apply-item!          engine/apply-item!)
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
