
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
              [plugins.item-editor.colors.views :as colors.views]
              [plugins.item-editor.core.subs    :as core.subs]
              [plugins.item-editor.core.views   :as core.views]
              [plugins.item-editor.form.subs    :as form.subs]
              [plugins.item-editor.form.views   :as form.views]
              [plugins.item-editor.routes.subs  :as routes.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-editor.core.subs
(def get-current-item-id core.subs/get-current-item-id)

; plugins.item-editor.colors.views
(def color-selector colors.views/color-selector)
(def color-stamp    colors.views/color-stamp)

; plugins.item-editor.core.views
(def delete-item-button core.views/delete-item-button)
(def copy-item-button   core.views/copy-item-button)
(def save-item-button   core.views/save-item-button)
(def error-body         core.views/error-body)
(def header             core.views/header)
(def body               core.views/body)

; plugins.item-editor.form.subs
(def form-completed? form.subs/form-completed?)

; plugins.item-editor.form.views
(def item-label         form.views/item-label)
(def input-group-header form.views/input-group-header)
(def description-field  form.views/description-field)

; plugins.item-editor.routes.subs
(def get-item-route routes.subs/get-item-route)
