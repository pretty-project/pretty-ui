
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.4.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-db.collection-handler
    (:require [x.mid-db.collection-handler :as collection-handler]
              [x.server-core.api           :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.collection-handler
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
(def store-collection!                      collection-handler/store-collection!)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:db/store-collection! [:collection :path] [{...} {...}]]
(a/reg-event-db :db/store-collection! store-collection!)
