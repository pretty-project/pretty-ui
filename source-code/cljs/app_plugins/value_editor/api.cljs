
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.value-editor.api
    (:require [app-plugins.value-editor.engine]
              [app-plugins.value-editor.events]
              [app-plugins.value-editor.effects]
              [app-plugins.value-editor.views]
              [app-plugins.value-editor.subs :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.value-editor.subs
(def get-editor-value subs/get-editor-value)
