
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.09.26
; Description:
; Version: v0.3.8
; Compatibility: x3.9.9



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.document-handler
    (:require [x.mid-db.document-handler :as document-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.document-handler
(def assoc-document-value            document-handler/assoc-document-value)
(def dissoc-document-value           document-handler/dissoc-document-value)
(def get-document-value              document-handler/get-document-value)
(def document->document-id           document-handler/document->document-id)
(def document->unidentified-document document-handler/document->unidentified-document)
(def document->pure-document         document-handler/document->pure-document)
(def document->identified-document   document-handler/document->identified-document)
