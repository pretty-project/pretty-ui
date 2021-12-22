
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.4.6
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.engine
    (:require [mid-plugins.item-editor.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-editor.engine
(def editor-uri              engine/editor-uri)
(def form-id                 engine/form-id)
(def item-id->new-item?      engine/item-id->new-item?)
(def item-id->form-label     engine/item-id->form-label)
(def new-item-label          engine/new-item-label)
(def unnamed-item-label      engine/unnamed-item-label)
(def request-id              engine/request-id)
(def mutation-name           engine/mutation-name)
(def resolver-id             engine/resolver-id)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
(def parent-uri              engine/parent-uri)
(def render-event            engine/render-event)
(def dialog-id               engine/dialog-id)
