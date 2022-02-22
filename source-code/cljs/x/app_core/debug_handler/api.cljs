
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.22
; Description:
; Version: v0.4.6
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.debug-handler.api
    (:require [x.app-core.debug-handler.engine]
              [x.app-core.debug-handler.side-effects]
              [x.app-core.debug-handler.events :as events]
              [x.app-core.debug-handler.subs   :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-core.debug-handler.events
(def set-debug-mode! events/set-debug-mode!)

; x.app-core.debug-handler.subs
(def get-debug-mode       subs/get-debug-mode)
(def debug-mode-detected? subs/debug-mode-detected?)
