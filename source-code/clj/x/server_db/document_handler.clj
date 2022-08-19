
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.3.8
; Compatibility: x4.1.5




;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-db.document-handler
    (:require [x.mid-db.document-handler :as document-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.document-handler
(def document->namespace                document-handler/document->namespace)
(def document->document-namespaced?     document-handler/document->document-namespaced?)
(def document->namespaced-document      document-handler/document->namespaced-document)
(def document->non-namespaced-document  document-handler/document->non-namespaced-document)
(def assoc-document-value               document-handler/assoc-document-value)
(def dissoc-document-value              document-handler/dissoc-document-value)
(def get-document-value                 document-handler/get-document-value)
(def document->document-id              document-handler/document->document-id)
(def document->unidentified-document    document-handler/document->unidentified-document)
(def document->pure-document            document-handler/document->pure-document)
(def document->identified-document      document-handler/document->identified-document)
