
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.3.8
; Compatibility: x4.1.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-db.document-handler
    (:require [x.mid-db.document-handler :as document-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.document-handler
(def document-path->collection-name     document-handler/document-path->collection-name)
(def document-path->document-id         document-handler/document-path->document-id)
(def item-key->non-namespaced-item-key  document-handler/item-key->non-namespaced-item-key)
(def item-key->namespaced-item-key      document-handler/item-key->namespaced-item-key)
(def document->namespace                document-handler/document->namespace)
(def document->namespace?               document-handler/document->namespace?)
(def document->document-namespaced?     document-handler/document->document-namespaced?)
(def document->document-non-namespaced? document-handler/document->document-non-namespaced?)
(def document->namespaced-document      document-handler/document->namespaced-document)
(def document->non-namespaced-document  document-handler/document->non-namespaced-document)
(def document-contains-key?             document-handler/document-contains-key?)
(def assoc-document-value               document-handler/assoc-document-value)
(def dissoc-document-value              document-handler/dissoc-document-value)
(def get-document-value                 document-handler/get-document-value)
(def document->document-id              document-handler/document->document-id)
(def document->unidentified-document    document-handler/document->unidentified-document)
(def document->pure-document            document-handler/document->pure-document)
(def document->identified-document      document-handler/document->identified-document)
(def document->identified-document?     document-handler/document->identified-document?)
(def document->non-identified-document? document-handler/document->non-identified-document?)
(def document->ordered-document         document-handler/document->ordered-document)
(def document->ordered-document?        document-handler/document->ordered-document?)
(def document->document-dex             document-handler/document->document-dex)
(def document->item-value               document-handler/document->item-value)
(def document->item-key                 document-handler/document->item-key)
