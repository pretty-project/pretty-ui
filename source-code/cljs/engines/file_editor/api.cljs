
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.api
    (:require [engines.file-editor.backup.events]
              [engines.file-editor.backup.subs]
              [engines.file-editor.body.effects]
              [engines.file-editor.body.events]
              [engines.file-editor.core.effects]
              [engines.file-editor.download.effects]
              [engines.file-editor.download.events]
              [engines.file-editor.download.subs]
              [engines.file-editor.transfer.subs]
              [engines.file-editor.update.effects]
              [engines.file-editor.update.subs]
              [engines.file-editor.body.subs   :as body.subs]
              [engines.file-editor.backup.subs :as backup.subs]
              [engines.file-editor.body.views  :as body.views]
              [engines.file-editor.core.events :as core.events]
              [engines.file-editor.core.subs   :as core.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.file-editor.body.subs
(def body-did-mount? body.subs/body-did-mount?)

; engines.file-editor.backup.subs
(def form-changed? backup.subs/form-changed?)

; engines.file-editor.body.views
(def body body.views/body)

; engines.file-editor.core.events
(def set-meta-item! core.events/set-meta-item!)

; engines.file-editor.core.subs
(def get-meta-item core.subs/get-meta-item)
