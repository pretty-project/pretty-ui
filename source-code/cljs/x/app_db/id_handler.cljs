
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.21
; Description:
; Version: v0.2.8
; Compatibility: x4.3.4



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.id-handler
    (:require [x.mid-db.id-handler :as id-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.id-handler
(def document-entity?             id-handler/document-entity?)
(def document-link?               id-handler/document-link?)
(def document-id->document-link   id-handler/document-id->document-link)
(def item-id->document-link       id-handler/item-id->document-link)
(def document-link->document-id   id-handler/document-link->document-id)
(def document-link->item-id       id-handler/document-link->item-id)
(def document-link->namespace     id-handler/document-link->namespace)
(def document-link->namespace?    id-handler/document-link->namespace?)
(def document-id->document-entity id-handler/document-id->document-entity)
(def item-id->document-entity     id-handler/item-id->document-entity)
(def document-entity->document-id id-handler/document-entity->document-id)
(def document-entity->item-id     id-handler/document-entity->item-id)
