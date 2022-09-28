
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-picker.api
    (:require [pathom.api]
              [plugins.item-editor.body.effects]
              [plugins.item-editor.body.events]
              [plugins.item-editor.body.subs]
              [plugins.item-editor.core.effects]
              [plugins.item-editor.download.effects]
              [plugins.item-editor.download.events]
              [plugins.item-editor.download.subs]
              [plugins.item-editor.transfer.subs]
              [plugins.item-editor.backup.subs :as backup.subs]
              [plugins.item-editor.body.views  :as body.views]
              [plugins.item-lister.core.events :as core.events]
              [plugins.item-editor.core.subs   :as core.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-editor.body.views
(def body body.views/body)

; plugins.item-editor.core.events
(def set-meta-item! core.events/set-meta-item!)

; plugins.item-editor.core.subs
(def get-meta-item       core.subs/get-meta-item)
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
