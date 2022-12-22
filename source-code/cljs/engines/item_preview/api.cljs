
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-preview.api
    (:require [engines.item-preview.body.effects]
              [engines.item-preview.body.events]
              [engines.item-preview.body.subs]
              [engines.item-preview.core.effects]
              [engines.item-preview.download.effects]
              [engines.item-preview.download.events]
              [engines.item-preview.download.subs]
              [engines.item-preview.transfer.subs]
              [engines.item-preview.body.views  :as body.views]
              [engines.item-preview.core.events :as core.events]
              [engines.item-preview.core.subs   :as core.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-preview.body.views
(def body body.views/body)

; engines.item-preview.core.events
(def set-meta-item! core.events/set-meta-item!)

; engines.item-preview.core.subs
(def get-meta-item       core.subs/get-meta-item)
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
