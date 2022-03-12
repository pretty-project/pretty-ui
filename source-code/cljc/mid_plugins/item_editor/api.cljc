
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-editor.api
    (:require [mid-plugins.item-editor.editor-handler.engine :as editor-handler.engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-editor.editor-handler.engine
(def value-path editor-handler.engine/value-path)
