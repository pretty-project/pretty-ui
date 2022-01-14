
(ns mongo-db.api
    (:require [mongo-db.adaptation]
              [mongo-db.connection]
              [mongo-db.engine]
              [mongo-db.actions   :as actions]
              [mongo-db.pipelines :as pipelines]
              [mongo-db.reader    :as reader]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mongo-db.actions
(def insert-document!    actions/insert-document!)
(def insert-documents!   actions/insert-documents!)
(def save-document!      actions/save-document!)
(def save-documents!     actions/save-documents!)
(def update-document!    actions/update-document!)
(def update-documents!   actions/update-documents!)
(def upsert-document!    actions/upsert-document!)
(def upsert-documents!   actions/upsert-documents!)
(def merge-document!     actions/merge-document!)
(def merge-documents!    actions/merge-documents!)
(def remove-document!    actions/remove-document!)
(def remove-documents!   actions/remove-documents!)
(def duplicate-document! actions/duplicate-document!)
(def reorder-documents!  actions/reorder-documents!)

; mongo-db.pipelines
(def filter-pattern->filter-query   pipelines/filter-pattern->filter-query)
(def search-pattern->search-query   pipelines/search-pattern->search-query)
(def sort-pattern->sort-query       pipelines/sort-pattern->sort-query)
(def field-pattern->field-operation pipelines/field-pattern->field-operation)
(def get-pipeline                   pipelines/get-pipeline)
(def count-pipeline                 pipelines/count-pipeline)

; mongo-db.reader
(def get-collection-namespace    reader/get-collection-namespace)
(def get-all-document-count      reader/get-all-document-count)
(def get-document-count-by-query reader/get-document-count-by-query)
(def get-all-documents           reader/get-all-documents)
(def get-documents-by-query      reader/get-documents-by-query)
(def get-document-by-query       reader/get-document-by-query)
(def get-document-by-id          reader/get-document-by-id)
(def document-exists?            reader/document-exists?)
(def count-documents-by-pipeline reader/count-documents-by-pipeline)
(def get-documents-by-pipeline   reader/get-documents-by-pipeline)
