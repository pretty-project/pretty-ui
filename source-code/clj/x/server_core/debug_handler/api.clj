
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.22
; Description:
; Version: v0.2.0
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.debug-handler.api
    (:require [x.server-core.debug-handler.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-core.debug-handler.engine
(def request->debug-mode engine/request->debug-mode)
