
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-browser.api
    (:require [mid-plugins.item-browser.engine :as engine]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  More info: app-plugins.item-browser.api
;             server-plugins.item-browser.api
;
; @usage
;  (ns my-namespace (:require [mid-plugins.item-browser.api :as item-browser]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-browser.engine
(def request-id              engine/request-id)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
