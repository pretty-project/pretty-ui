
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.0
; Compatibility: x4.6.0



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
(def component-id            engine/component-id)
(def load-extension-event    engine/load-extension-event)
