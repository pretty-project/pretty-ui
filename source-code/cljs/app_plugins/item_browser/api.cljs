
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.api
    (:require [app-plugins.item-browser.dialogs]
              [app-plugins.item-browser.effects]
              [app-plugins.item-browser.engine :as engine]
              [app-plugins.item-browser.events :as events]
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

; app-plugins.item-browser.events
(def toggle-item-selection! events/toggle-item-selection!)

; app-plugins.item-editor.subs
(def get-current-item-id    subs/get-current-item-id)
(def toggle-item-selection? subs/toggle-item-selection?)

; app-plugins.item-browser.views
(def go-home-button views/go-home-button)
(def go-up-button   views/go-up-button)
(def header         views/header)
(def body           views/body)
