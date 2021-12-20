
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.8
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.api
    (:require [server-plugins.item-editor.events]
              [server-plugins.item-editor.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; server-plugins.item-editor.engine
(def editor-uri engine/editor-uri)
(def form-id    engine/form-id)
