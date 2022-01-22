
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.3.4
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.api
    (:require [server-plugins.item-editor.subs]
              [server-plugins.item-editor.engine    :as engine]
              [server-plugins.item-editor.events    :as events]
              [server-plugins.item-editor.resolvers :as resolvers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; server-plugins.item-editor.engine
(def editor-uri engine/editor-uri)
(def form-id    engine/form-id)
(def request-id engine/request-id)

; server-plugins.item-editor.events
(def initialize! events/initialize!)

; server-plugins.item-editor.resolvers
(def get-item-suggestions resolvers/get-item-suggestions)
