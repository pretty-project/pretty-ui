
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.15
; Description:
; Version: v0.4.2
; Compatibility: x4.4.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns local-db.api
    (:require [local-db.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; local-db.engine
(def get-collection        engine/get-collection)
(def set-collection!       engine/set-collection!)
(def filter-documents      engine/filter-documents)
(def filter-document       engine/filter-document)
(def match-documents       engine/match-documents)
(def match-document        engine/match-document)
(def get-documents-kv      engine/get-documents-kv)
(def get-document-kv       engine/get-document-kv)
(def get-documents         engine/get-documents)
(def get-document          engine/get-document)
(def get-document-item     engine/get-document-item)
(def add-document!         engine/add-document!)
(def remove-documents!     engine/remove-documents!)
(def remove-document!      engine/remove-document!)
(def set-document!         engine/set-document!)
(def update-document!      engine/update-document!)
(def update-document-item! engine/update-document-item!)
(def document-exists?      engine/document-exists?)
