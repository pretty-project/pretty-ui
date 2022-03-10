
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.api
    (:require [app-plugins.item-editor.dialogs]
              [app-plugins.item-editor.effects]
              [app-plugins.item-editor.events]
              [app-plugins.item-editor.queries]
              [app-plugins.item-editor.engine :as engine]
              [app-plugins.item-editor.subs   :as subs]
              [app-plugins.item-editor.views  :as views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.item-editor.engine
(def value-path engine/value-path)
(def form-id    engine/form-id)
(def request-id engine/request-id)

; app-plugins.item-editor.subs
(def get-current-item-id subs/get-current-item-id)
(def get-item-route      subs/get-item-route)

; app-plugins.item-editor.views
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
