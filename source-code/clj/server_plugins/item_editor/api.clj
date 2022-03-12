
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.api
    (:require [server-plugins.item-editor.editor-handler.effects]
              [server-plugins.item-editor.item-handler.resolvers]
              [server-plugins.item-editor.route-handler.effects]
              [server-plugins.item-editor.transfer-handler.effects]
              [server-plugins.item-editor.download-handler.engine :as download-handler.engine]
              [server-plugins.item-editor.editor-handler.engine   :as editor-handler.engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; server-plugins.item-editor.download-handler.engine
(def env->item-suggestions download-handler.engine/env->item-suggestions)

; server-plugins.item-editor.editor-handler.engine
(def value-path editor-handler.engine/value-path)
