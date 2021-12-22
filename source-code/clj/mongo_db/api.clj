
(ns mongo-db.api
    (:require [mongo-db.engine    :as engine]
              [mongo-db.pipelines :as pipelines]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mongo-db.engine
(def get-all-documents           engine/get-all-documents)
(def get-documents-by-query      engine/get-documents-by-query)
(def get-document-by-query       engine/get-document-by-query)
(def get-document-by-id          engine/get-document-by-id)
(def document-exists?            engine/document-exists?)
(def add-document!               engine/add-document!)
(def add-documents!              engine/add-documents!)
(def upsert-document!            engine/upsert-document!)
(def update-document!            engine/update-document!)
(def merge-document!             engine/merge-document!)
(def merge-documents!            engine/merge-documents!)
(def remove-document!            engine/remove-document!)
(def remove-documents!           engine/remove-documents!)
(def duplicate-document!         engine/duplicate-document!)
(def reorder-documents!          engine/reorder-documents!)
(def count-documents-by-pipeline engine/count-documents-by-pipeline)
(def get-documents-by-pipeline   engine/get-documents-by-pipeline)

; mongo-db.pipelines
(def filter-pattern->query-pipeline pipelines/filter-pattern->query-pipeline)
(def search-pattern->query-pipeline pipelines/search-pattern->query-pipeline)
(def sort-pattern->sort-pipeline    pipelines/sort-pattern->sort-pipeline)
