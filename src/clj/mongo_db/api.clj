
(ns mongo-db.api
    (:require
        [mongo-db.engine :as engine]
        [mongo-db.pipelines :as pipelines]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mongo.engine
(def get-documents-by-query      engine/get-documents-by-query)
(def get-document-by-query       engine/get-document-by-query)
(def get-document-by-id          engine/get-document-by-id)
(def document-exists?            engine/document-exists?)
(def add-document!               engine/add-document!)
(def update-document!            engine/update-document!)
(def remove-document!            engine/remove-document!)
(def duplicate-document!         engine/duplicate-document!)
(def reorder-documents!          engine/reorder-documents!)
(def count-documents-by-pipeline engine/count-documents-by-pipeline)
(def get-documents-by-pipeline   engine/get-documents-by-pipeline)
(def search-props->pipeline      pipelines/search-props->pipeline)
