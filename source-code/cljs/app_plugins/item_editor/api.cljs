
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.3.8
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.api
    (:require [app-plugins.item-editor.dialogs]
              [app-plugins.item-editor.queries]
              [app-plugins.item-editor.engine :as engine]
              [app-plugins.item-editor.events :as events]
              [app-plugins.item-editor.subs   :as subs]
              [app-plugins.item-editor.views  :as views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.item-editor.engine
(def editor-uri engine/editor-uri)
(def form-id    engine/form-id)
(def request-id engine/request-id)

; app-plugins.item-editor.engine
(def set-current-item-id! events/set-current-item-id!)

; app-plugins.item-editor.subs
(def get-current-item-id subs/get-current-item-id)
(def get-body-props      subs/get-body-props)
(def get-header-props    subs/get-header-props)
(def get-view-props      subs/get-view-props)

; app-plugins.item-editor.views
(def delete-item-button views/delete-item-button)
(def copy-item-button   views/copy-item-button)
(def save-item-button   views/save-item-button)
(def item-label         views/item-label)
(def color-selector     views/color-selector)
(def color-stamp        views/color-stamp)
(def description-field  views/description-field)
(def error-body         views/error-body)
(def header             views/header)
(def view               views/view)
