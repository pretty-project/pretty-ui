
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.api
    (:require [plugins.item-editor.backup.events]
              [plugins.item-editor.backup.subs]
              [plugins.item-editor.colors.effects]
              [plugins.item-editor.core.effects]
              [plugins.item-editor.core.events]
              [plugins.item-editor.download.effects]
              [plugins.item-editor.download.events]
              [plugins.item-editor.download.subs]
              [plugins.item-editor.update.effects]
              [plugins.item-editor.update.events]
              [plugins.item-editor.update.subs]
              [plugins.item-editor.core.subs      :as core.subs]
              [plugins.item-editor.core.views     :as core.views]
              [plugins.item-editor.engine.helpers :as engine.helpers]
              [plugins.item-editor.routes.subs    :as routes.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-editor.core.subs
(def get-current-item-id core.subs/get-current-item-id)

; plugins.item-editor.core.views
(def delete-item-button views/delete-item-button)
(def copy-item-button   views/copy-item-button)
(def save-item-button   views/save-item-button)
(def item-label         views/item-label)
(def color-selector     views/color-selector)
(def color-stamp        views/color-stamp)
(def input-group-header views/input-group-header)
(def description-field  views/description-field)
(def error-body         views/error-body)
(def header             views/header)
(def body               views/body)

; plugins.item-editor.engine.helpers
(def value-path engine.helpers/value-path)

; plugins.item-editor.routes.subs
(def get-item-route routes.subs/get-item-route)
