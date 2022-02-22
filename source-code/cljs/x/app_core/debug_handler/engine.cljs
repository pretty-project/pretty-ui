
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.22
; Description:
; Version: v0.4.6
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.debug-handler.engine
    (:require [x.mid-core.debug-handler.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.debug-handler.engine
(def query-string->debug-mode engine/query-string->debug-mode)
