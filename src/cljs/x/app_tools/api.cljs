
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v0.1.4
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.api
    (:require [x.app-tools.clipboard]
              [x.app-tools.help-center.api]
              [x.app-tools.pdf]
              [x.app-tools.scheduler]
              [x.app-tools.editor     :as editor]
              [x.app-tools.file-saver :as file-saver]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-tools.editor
(def get-editor-value editor/get-editor-value)
