
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.2.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.api
    (:require [app-plugins.item-editor.dialogs :as dialogs]
              [app-plugins.item-editor.engine  :as engine]
              [app-plugins.item-editor.views   :as views]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  More info: server-plugins.item-editor.api
;
; @usage
;  (a/reg-event-fx :my-extension/render-item-editor! (fn [_ _] ...))
;
;  (a/dispatch [:router/go-to! "/my-extension/my-item"])



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.item-editor.engine
(def editor-uri        engine/editor-uri)
(def form-id           engine/form-id)
(def get-body-props    engine/get-body-props)
(def get-header-props  engine/get-header-props)

; app-plugins.item-editor.views
(def input-group-label    views/input-group-label)
(def input-group-footer   views/input-group-footer)
(def form-footer          views/form-footer)
(def form-header          views/form-header)
(def color-selector       views/color-selector)
(def color-stamp          views/color-stamp)
(def error-body           views/error-body)
(def header               views/header)
