
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.17
; Description:
; Version: v0.3.4
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.value-editor.api
    (:require [app-plugins.value-editor.engine]
              [app-plugins.value-editor.events]
              [app-plugins.value-editor.views]
              [app-plugins.value-editor.subs :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.value-editor.subs
(def get-editor-value subs/get-editor-value)
