
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.3.8
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.engine
    (:require [mid-plugins.item-browser.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-browser.engine
(def browser-uri             engine/browser-uri)
(def request-id              engine/request-id)
(def data-item-path          engine/data-item-path)
(def meta-item-path          engine/meta-item-path)
(def resolver-id             engine/resolver-id)
(def collection-name         engine/collection-name)
(def transfer-id             engine/transfer-id)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
(def load-extension-event    engine/load-extension-event)
(def item-clicked-event      engine/item-clicked-event)
