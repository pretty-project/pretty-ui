
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.api
    (:require [pathom.api]
              [engines.item-editor.backup.events]
              [engines.item-editor.body.effects]
              [engines.item-editor.body.events]
              [engines.item-editor.body.subs]
              [engines.item-editor.core.effects]
              [engines.item-editor.download.effects]
              [engines.item-editor.download.events]
              [engines.item-editor.download.subs]
              [engines.item-editor.routes.effects]
              [engines.item-editor.transfer.subs]
              [engines.item-editor.update.effects]
              [engines.item-editor.update.subs]
              [engines.item-editor.backup.subs :as backup.subs]
              [engines.item-editor.body.views  :as body.views]
              [engines.item-editor.core.events :as core.events]
              [engines.item-editor.core.subs   :as core.subs]
              [engines.item-editor.routes.subs :as routes.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-editor.backup.subs
(def form-changed? backup.subs/form-changed?)

; engines.item-editor.body.views
(def body body.views/body)

; engines.item-editor.core.events
(def set-meta-item! core.events/set-meta-item!)

; engines.item-editor.core.subs
(def get-meta-item       core.subs/get-meta-item)
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
(def export-current-item core.subs/export-current-item)
(def editing-item?       core.subs/editing-item?)

; engines.item-editor.routes.subs
(def get-item-route routes.subs/get-item-route)
(def get-edit-route routes.subs/get-edit-route)
