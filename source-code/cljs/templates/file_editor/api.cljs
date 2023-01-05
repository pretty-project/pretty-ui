
(ns templates.file-editor.api
    (:require [engines.file-editor.api]
              [templates.file-editor.core.subs]
              [templates.file-editor.update.effects]
              [templates.file-editor.body.views   :as body.views]
              [templates.file-editor.header.views :as header.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; templates.file-editor.body.views
(def body body.views/body)

; templates.file-editor.header.views
(def header header.views/header)
