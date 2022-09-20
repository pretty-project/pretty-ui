
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.file-editor.api
    (:require [pathom.api]
              [plugins.file-editor.backup.events]
              [plugins.file-editor.backup.subs]
              [plugins.file-editor.body.effects]
              [plugins.file-editor.body.events]
              [plugins.file-editor.body.subs]
              [plugins.file-editor.core.effects]
              [plugins.file-editor.core.events]
              [plugins.file-editor.core.subs]
              [plugins.file-editor.download.effects]
              [plugins.file-editor.download.events]
              [plugins.file-editor.download.subs]
              [plugins.file-editor.routes.effects]
              [plugins.file-editor.routes.subs]
              [plugins.file-editor.transfer.subs]
              [plugins.file-editor.update.effects]
              [plugins.file-editor.update.subs]
              [plugins.file-editor.backup.subs :as backup.subs]
              [plugins.file-editor.body.views  :as body.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.file-editor.backup.subs
(def form-changed? backup.subs/form-changed?)

; plugins.file-editor.body.views
(def body body.views/body)
