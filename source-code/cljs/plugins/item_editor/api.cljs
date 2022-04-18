
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.api
    (:require [plugins.item-editor.backup.events]
              [plugins.item-editor.backup.subs]
              [plugins.item-editor.core.effects]
              [plugins.item-editor.core.events]
              [plugins.item-editor.core.helpers]
              [plugins.item-editor.download.effects]
              [plugins.item-editor.download.events]
              [plugins.item-editor.download.subs]
              [plugins.item-editor.header.events]
              [plugins.item-editor.header.subs]
              [plugins.item-editor.mount.effects]
              [plugins.item-editor.mount.events]
              [plugins.item-editor.mount.subs]
              [plugins.item-editor.routes.effects]
              [plugins.item-editor.routes.events]
              [plugins.item-editor.update.effects]
              [plugins.item-editor.update.events]
              [plugins.item-editor.update.subs]
              [plugins.item-editor.body.subs    :as body.subs]
              [plugins.item-editor.body.views   :as body.views]
              [plugins.item-editor.core.subs    :as core.subs]
              [plugins.item-editor.footer.views :as footer.views]
              [plugins.item-editor.header.views :as header.views]
              [plugins.item-editor.routes.subs  :as routes.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-editor.body.subs
(def form-completed? body.subs/form-completed?)
(def form-changed?   body.subs/form-changed?)

; plugins.item-editor.body.views
(def item-label body.views/item-label)
(def error-body body.views/error-body)
(def body       body.views/body)

; plugins.item-editor.core.subs
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
(def editing-item?       core.subs/editing-item?)

; plugins.item-editor.footer.views
(def revert-item-icon-button footer.views/revert-item-icon-button)
(def revert-item-button      footer.views/revert-item-button)
(def revert-item-block       footer.views/revert-item-block)
(def delete-item-icon-button footer.views/delete-item-button)
(def delete-item-button      footer.views/delete-item-icon-button)
(def delete-item-block       footer.views/delete-item-block)
(def copy-item-icon-button   footer.views/copy-item-button)
(def copy-item-button        footer.views/copy-item-icon-button)
(def copy-item-block         footer.views/copy-item-block)
(def save-item-icon-button   footer.views/save-item-icon-button)
(def save-item-button        footer.views/save-item-button)
(def save-item-block         footer.views/save-item-block)
(def footer                  footer.views/footer)

; plugins.item-editor.header.views
(def header header.views/header)

; plugins.item-editor.routes.subs
(def get-item-route routes.subs/get-item-route)
