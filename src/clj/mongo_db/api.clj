
(ns mongo-db.api
    (:require [mongo-db.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mongo.engine
(def get-documents-by-query        engine/get-documents-by-query)
(def get-document-by-query         engine/get-document-by-query)
(def get-document-by-id            engine/get-document-by-id)
(def document-exists?              engine/document-exists?)
(def add-document!                 engine/add-document!)
(def update-document!              engine/update-document!)
(def remove-document!              engine/remove-document!)
(def duplicate-document!           engine/duplicate-document!)
(def reorder-documents!            engine/reorder-documents!)
(def count-documents-with-pipeline engine/count-documents-with-pipeline)
(def find-documents-with-pipeline  engine/find-documents-with-pipeline)
