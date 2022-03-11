
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.engine
    (:require [mid-plugins.item-editor.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-editor.engine
(def value-path         engine/value-path)
(def add-item-label     engine/add-item-label)
(def edit-item-label    engine/edit-item-label)
(def new-item-label     engine/new-item-label)
(def unnamed-item-label engine/unnamed-item-label)
(def collection-name    engine/collection-name)
(def transfer-id        engine/transfer-id)
(def route-id           engine/route-id)
(def route-template     engine/route-template)
(def base-route         engine/base-route)
(def component-id       engine/component-id)
