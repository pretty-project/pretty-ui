
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.3.8
; Compatibility: x4.1.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-db.transfer-handler
    (:require [x.mid-db.transfer-handler :as transfer-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.transfer-handler
(def document-id->data-item-id            transfer-handler/document-id->data-item-id)
(def data-item-value->document-item-value transfer-handler/data-item-value->document-item-value)
(def document-item-value->data-item-value transfer-handler/document-item-value->data-item-value)
(def data-item->document                  transfer-handler/data-item->document)
(def document->data-item                  transfer-handler/document->data-item)
(def partition->collection                transfer-handler/partition->collection)
(def collection->data-items               transfer-handler/collection->data-items)
(def collection->data-order               transfer-handler/collection->data-order)
(def collection->partition                transfer-handler/collection->partition)
(def collection->map                      transfer-handler/collection->map)
(def export-partition                     transfer-handler/export-partition)
(def import-collection!                   transfer-handler/import-collection!)
