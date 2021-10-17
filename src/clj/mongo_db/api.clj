
(ns mongo-db.api
    (:require [mongo-db.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mongo.engine
(def update-document!              engine/update-document!)
(def remove-document!              engine/remove-document!)
(def duplicate-document!           engine/duplicate-document!)
(def reorder-documents!            engine/reorder-documents!)
(def count-documents-with-pipeline engine/count-documents-with-pipeline)
(def find-documents-with-pipeline  engine/find-documents-with-pipeline)
