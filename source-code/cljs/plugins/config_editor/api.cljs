
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.config-editor.api
    (:require [pathom.api]
              [plugins.config-editor.backup.events]
              [plugins.config-editor.backup.subs]
              [plugins.config-editor.body.effects]
              [plugins.config-editor.body.events]
              [plugins.config-editor.body.subs]
              [plugins.config-editor.core.effects]
              [plugins.config-editor.core.events]
              [plugins.config-editor.download.effects]
              [plugins.config-editor.download.events]
              [plugins.config-editor.download.subs]
              [plugins.config-editor.routes.effects]
              [plugins.config-editor.routes.subs]
              [plugins.config-editor.transfer.subs]
              [plugins.config-editor.update.effects]
              [plugins.config-editor.update.subs]
              [plugins.config-editor.backup.subs :as backup.subs]
              [plugins.config-editor.body.views  :as body.views]
              [plugins.config-editor.core.subs   :as core.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.config-editor.backup.subs
(def form-changed? backup.subs/form-changed?)

; plugins.config-editor.body.views
(def body body.views/body)

; plugins.config-editor.core.subs
