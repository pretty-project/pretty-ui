
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.1.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.api
    (:require [app-plugins.item-browser.engine :as engine]
              [app-plugins.item-browser.views  :as views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.item-browser.engine
(def request-id              engine/request-id)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
(def get-derived-item-id     engine/get-derived-item-id)
(def get-current-path        engine/get-current-path)
(def at-home?                engine/at-home?)
(def get-header-props        engine/get-header-props)

; plugins.item-browser.views
