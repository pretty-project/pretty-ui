
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.api
    (:require [server-plugins.item-lister.engine :as engine]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  More info: app-plugins.item-lister.api
;
; @usage
;  (ns my-namespace (:require [server-plugins.item-lister.api :as item-lister]))
;
; @usage
;  (defresolver get-my-type-items [_] ...)



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; server-plugins.item-lister.engine
(def extension-namespace   engine/extension-namespace)
(def request-id            engine/request-id)
(def resolver-id           engine/resolver-id)
(def new-item-uri          engine/new-item-uri)
(def add-new-item-event-id engine/add-new-item-event-id)
(def route-id              engine/route-id)
