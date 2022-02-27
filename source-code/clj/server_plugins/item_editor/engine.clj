
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.engine
    (:require [mid-plugins.item-editor.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-editor.engine
(def editor-uri            engine/editor-uri)
(def form-id               engine/form-id)
(def request-id            engine/request-id)
(def item-id->new-item?    engine/item-id->new-item?)
(def item-id->editor-title engine/item-id->editor-title)
(def new-item-label        engine/new-item-label)
(def unnamed-item-label    engine/unnamed-item-label)
(def mutation-name         engine/mutation-name)
(def resolver-id           engine/resolver-id)
(def collection-name       engine/collection-name)
(def transfer-id           engine/transfer-id)
(def route-id              engine/route-id)
(def route-template        engine/route-template)
(def parent-uri            engine/parent-uri)
(def component-id           engine/component-id)
(def dialog-id             engine/dialog-id)
(def load-extension-event  engine/load-extension-event)
