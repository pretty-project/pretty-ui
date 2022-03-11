
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.api
    (:require [server-plugins.item-editor.effects]
              [server-plugins.item-editor.engine    :as engine]
              [server-plugins.item-editor.resolvers :as resolvers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; server-plugins.item-editor.engine
(def value-path engine/value-path)
(def form-id    engine/form-id)

; server-plugins.item-editor.resolvers
(def get-item-suggestions resolvers/get-item-suggestions)
