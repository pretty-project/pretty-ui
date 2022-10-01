
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
              [plugins.item-picker.body.effects]
              [plugins.item-picker.body.events]
              [plugins.item-picker.body.subs]
              [plugins.item-picker.core.effects]
              [plugins.item-picker.download.effects]
              [plugins.item-picker.download.events]
              [plugins.item-picker.download.subs]
              [plugins.item-picker.transfer.subs]
              [plugins.item-picker.backup.subs :as backup.subs]
              [plugins.item-picker.body.views  :as body.views]
              [plugins.item-lister.core.events :as core.events]
              [plugins.item-picker.core.subs   :as core.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-picker.body.views
(def body body.views/body)

; plugins.item-picker.core.events
(def set-meta-item! core.events/set-meta-item!)

; plugins.item-picker.core.subs
(def get-meta-item       core.subs/get-meta-item)
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
