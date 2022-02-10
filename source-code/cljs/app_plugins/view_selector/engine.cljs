
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.5.6
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.view-selector.engine
    (:require [mid-plugins.view-selector.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.view-selector.engine
(def DEFAULT-VIEW-ID         engine/DEFAULT-VIEW-ID)
(def transfer-id             engine/transfer-id)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
(def route-string            engine/route-string)
(def extended-route-string   engine/extended-route-string)
(def load-extension-event    engine/load-extension-event)
