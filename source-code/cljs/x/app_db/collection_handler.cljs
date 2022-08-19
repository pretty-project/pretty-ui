
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

(ns x.app-db.collection-handler
    (:require [x.app-core.api              :as a :refer [r]]
              [x.mid-db.collection-handler :as collection-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.collection-handler
(def collection->namespace                  collection-handler/collection->namespace)
(def collection->namespaced-collection      collection-handler/collection->namespaced-collection)
(def collection->non-namespaced-collection  collection-handler/collection->non-namespaced-collection)
(def trim-collection                        collection-handler/trim-collection)
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
(def apply-document                         collection-handler/apply-document)
(def document-exists?                       collection-handler/document-exists?)
