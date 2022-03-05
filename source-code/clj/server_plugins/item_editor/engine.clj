
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.engine
    (:require [mid-plugins.item-editor.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-editor.engine
(def editor-uri         engine/editor-uri)
(def form-id            engine/form-id)
(def request-id         engine/request-id)
(def add-item-label     engine/add-item-label)
(def edit-item-label    engine/edit-item-label)
(def new-item-label     engine/new-item-label)
(def unnamed-item-label engine/unnamed-item-label)
(def mutation-name      engine/mutation-name)
(def resolver-id        engine/resolver-id)
(def collection-name    engine/collection-name)
(def component-id       engine/component-id)
(def dialog-id          engine/dialog-id)
