
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.api
    (:require [plugins.item-browser.backup.events]
              [plugins.item-browser.backup.subs]
              [plugins.item-browser.core.effects]
              [plugins.item-browser.core.events]
              [plugins.item-browser.download.effects]
              [plugins.item-browser.download.events]
              [plugins.item-browser.download.subs]
              [plugins.item-browser.mount.effects]
              [plugins.item-browser.mount.events]
              [plugins.item-browser.mount.subs]
              [plugins.item-browser.routes.events]
              [plugins.item-browser.update.effects]
              [plugins.item-browser.update.events]
              [plugins.item-browser.update.subs]
              [plugins.item-browser.core.subs    :as core.subs]
              [plugins.item-browser.core.views   :as core.views]
              [plugins.item-browser.items.events :as items.events]
              [plugins.item-browser.items.subs   :as items.subs]
              [plugins.item-browser.routes.subs  :as routes.subs]))



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
; - Az item-browser plugin az item-lister plugin alapjaira épül, ezért mindkét
;   plugin módosításait úgy kell elvégezni, hogy azok működése egymással összehangolt
;   maradjon!
; - Az item-browser plugin dokumentációjából hiányzó részeket az item-lister
;   plugin dokumentációjában keresd!



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-browser.core.subs
(def get-current-item-id core.subs/get-current-item-id)

; plugins.item-browser.core.views
(def go-home-button core.views/go-home-button)
(def go-up-button   core.views/go-up-button)
(def header         core.views/header)
(def body           core.views/body)

; plugins.item-browser.items.events
(def toggle-item-selection! items.events/toggle-item-selection!)

; plugins.item-browser.items.subs
(def toggle-item-selection? items.subs/toggle-item-selection?)

; plugins.item-browser.routes.subs
(def get-item-route routes.subs/get-item-route)
