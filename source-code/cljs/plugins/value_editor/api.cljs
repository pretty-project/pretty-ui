
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.value-editor.api
    (:require [plugins.value-editor.core.effects]
              [plugins.value-editor.core.events]
              [plugins.value-editor.core.subs :as core.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.value-editor.core.subs
(def get-editor-value core.subs/get-editor-value)
