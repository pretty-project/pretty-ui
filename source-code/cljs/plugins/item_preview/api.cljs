
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-preview.api
    (:require [pathom.api]
              [plugins.item-preview.body.effects]
              [plugins.item-preview.body.events]
              [plugins.item-preview.body.subs]
              [plugins.item-preview.core.effects]
              [plugins.item-preview.download.effects]
              [plugins.item-preview.download.events]
              [plugins.item-preview.download.subs]
              [plugins.item-preview.transfer.subs]
              [plugins.item-preview.body.views  :as body.views]
              [plugins.item-preview.core.events :as core.events]
              [plugins.item-preview.core.subs   :as core.subs]))

 

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-preview.body.views
(def body body.views/body)

; plugins.item-preview.core.events
(def set-meta-item! core.events/set-meta-item!)

; plugins.item-preview.core.subs
(def get-meta-item       core.subs/get-meta-item)
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
