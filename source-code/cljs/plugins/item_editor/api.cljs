
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.api
    (:require [plugins.item-editor.backup.events]
              [plugins.item-editor.body.effects]
              [plugins.item-editor.body.events]
              [plugins.item-editor.core.effects]
              [plugins.item-editor.core.events]
              [plugins.item-editor.download.effects]
              [plugins.item-editor.download.events]
              [plugins.item-editor.download.subs]
              [plugins.item-editor.footer.effects]
              [plugins.item-editor.footer.events]
              [plugins.item-editor.footer.subs]
              [plugins.item-editor.header.effects]
              [plugins.item-editor.header.events]
              [plugins.item-editor.header.subs]
              [plugins.item-editor.routes.effects]
              [plugins.item-editor.transfer.subs]
              [plugins.item-editor.update.effects]
              [plugins.item-editor.update.subs]
              [plugins.item-editor.backup.subs  :as backup.subs]
              [plugins.item-editor.body.subs    :as body.subs]
              [plugins.item-editor.body.views   :as body.views]
              [plugins.item-editor.core.subs    :as core.subs]
              [plugins.item-editor.footer.views :as footer.views]
              [plugins.item-editor.header.views :as header.views]
              [plugins.item-editor.routes.subs  :as routes.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-editor.backup.subs
(def form-changed? backup.subs/form-changed?)

; plugins.item-editor.body.subs
(def form-completed? body.subs/form-completed?)

; plugins.item-editor.body.views
(def item-label body.views/item-label)
(def body       body.views/body)

; plugins.item-editor.core.subs
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
(def editing-item?       core.subs/editing-item?)

; plugins.item-editor.footer.views
(def revert-item-block footer.views/revert-item-block)
(def cancel-item-block footer.views/cancel-item-block)
(def save-item-block   footer.views/save-item-block)
(def footer            footer.views/footer)

; plugins.item-editor.header.views
(def header header.views/header)

; plugins.item-editor.routes.subs
(def get-item-route routes.subs/get-item-route)
(def get-edit-route routes.subs/get-edit-route)
