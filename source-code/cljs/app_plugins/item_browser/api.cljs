
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.2.4
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.api
    (:require [app-plugins.item-browser.events]
              [app-plugins.item-browser.queries]
              [app-plugins.item-browser.engine :as engine]
              [app-plugins.item-browser.subs   :as subs]
              [app-plugins.item-browser.views  :as views]))



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  Az item-browser plugin az item-lister plugin alapjaira épül, ezért mindkét
;  plugin módosításait úgy kell elvégezni, hogy azok működése egymással összehangolt
;  maradjon! Az item-browser plugin dokumentációjából hiányzó részeket az item-lister
;  plugin dokumentációjában keresd!



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.item-browser.engine
(def browser-uri engine/browser-uri)
(def request-id  engine/request-id)

; app-plugins.item-browser.subs
(def get-current-item-id subs/get-current-item-id)
(def get-body-props      subs/get-body-props)
(def get-header-props    subs/get-header-props)
(def get-view-props      subs/get-view-props)

; app-plugins.item-browser.views
(def go-home-button views/go-home-button)
(def go-up-button   views/go-up-button)
(def header         views/header)
(def body           views/body)
(def view           views/view)
