
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.api
    (:require [plugins.item-editor.backup.events]
              [plugins.item-editor.backup.subs]
              [plugins.item-editor.colors.effects]
              [plugins.item-editor.core.effects]
              [plugins.item-editor.core.events]
              [plugins.item-editor.core.helpers]
              [plugins.item-editor.download.effects]
              [plugins.item-editor.download.events]
              [plugins.item-editor.download.subs]
              [plugins.item-editor.mount.effects]
              [plugins.item-editor.mount.events]
              [plugins.item-editor.mount.subs]
              [plugins.item-editor.update.effects]
              [plugins.item-editor.update.events]
              [plugins.item-editor.update.subs]
              [plugins.item-editor.colors.views      :as colors.views]
              [plugins.item-editor.control-bar.views :as control-bar.views]
              [plugins.item-editor.core.subs         :as core.subs]
              [plugins.item-editor.form.subs         :as form.subs]
              [plugins.item-editor.form.views        :as form.views]
              [plugins.item-editor.menu-bar.views    :as menu-bar.views]
              [plugins.item-editor.routes.subs       :as routes.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-editor.colors.views
(def color-selector colors.views/color-selector)

; plugins.item-editor.control-bar.views
(def revert-item-icon-button control-bar.views/revert-item-icon-button)
(def revert-item-button      control-bar.views/revert-item-button)
(def revert-item-block       control-bar.views/revert-item-block)
(def delete-item-icon-button control-bar.views/delete-item-button)
(def delete-item-button      control-bar.views/delete-item-icon-button)
(def delete-item-block       control-bar.views/delete-item-block)
(def copy-item-icon-button   control-bar.views/copy-item-button)
(def copy-item-button        control-bar.views/copy-item-icon-button)
(def copy-item-block         control-bar.views/copy-item-block)
(def save-item-icon-button   control-bar.views/save-item-icon-button)
(def save-item-button        control-bar.views/save-item-button)
(def save-item-block         control-bar.views/save-item-block)
(def footer                  control-bar.views/footer)

; plugins.item-editor.core.subs
(def get-current-item-id core.subs/get-current-item-id)
(def editing-item?       core.subs/editing-item?)

; plugins.item-editor.form.subs
(def form-completed? form.subs/form-completed?)

; plugins.item-editor.form.views
(def item-label         form.views/item-label)
(def input-group-header form.views/input-group-header)
(def description-field  form.views/description-field)
(def error-body         form.views/error-body)
(def body               form.views/body)

; plugins.item-editor.menu-bar.views
(def header menu-bar.views/header)

; plugins.item-editor.routes.subs
(def get-item-route routes.subs/get-item-route)
