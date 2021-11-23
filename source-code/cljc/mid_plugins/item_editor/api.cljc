
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-editor.api
    (:require [mid-plugins.item-editor.engine :as engine]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  More info: app-plugins.item-editor.api
;             server-plugins.item-editor.api
;
; @usage
;  (ns my-namespace (:require [mid-plugins.item-editor.api :as item-editor]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-editor.engine
(def extension-namespace engine/extension-namespace)
(def item-id->new-item?  engine/item-id->new-item?)
(def item-id->form-label engine/item-id->form-label)
(def item-id->item-uri   engine/item-id->item-uri)
(def request-id          engine/request-id)
(def mutation-name       engine/mutation-name)
(def form-id             engine/form-id)
(def route-id            engine/route-id)
(def route-template      engine/route-template)
