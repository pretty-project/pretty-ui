
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.api
    (:require [engines.item-handler.body.effects]
              [engines.item-handler.body.events]
              [engines.item-handler.core.effects]
              [engines.item-handler.download.effects]
              [engines.item-handler.download.events]
              [engines.item-handler.download.subs]
              [engines.item-handler.transfer.subs]
              [engines.item-handler.update.effects]
              [engines.item-handler.update.subs]
              [engines.item-handler.backup.events :as backup.events]
              [engines.item-handler.backup.subs   :as backup.subs]
              [engines.item-handler.body.subs     :as body.subs]
              [engines.item-handler.body.views    :as body.views]
              [engines.item-handler.core.events   :as core.events]
              [engines.item-handler.core.subs     :as core.subs]
              [engines.item-handler.update.subs   :as update.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-handler.body.subs
(def body-did-mount? body.subs/body-did-mount?)

; engines.item-handler.backup.events
(def revert-current-item! backup.events/revert-current-item!)
(def current-item-changed backup.events/current-item-changed)

; engines.item-handler.backup.subs
(def current-item-changed? backup.subs/current-item-changed?)

; engines.item-handler.body.views
(def body body.views/body)

; engines.item-handler.core.events
(def set-meta-item! core.events/set-meta-item!)
(def set-item-id!   core.events/set-item-id!)

; engines.item-handler.core.subs
(def get-meta-item          core.subs/get-meta-item)
(def get-current-item-id    core.subs/get-current-item-id)
(def get-current-item       core.subs/get-current-item)
(def get-current-item-value core.subs/get-current-item-value)
(def export-current-item    core.subs/export-current-item)
(def handling-item?         core.subs/handling-item?)

; engines.item-handler.update.subs
(def get-saved-item-id     update.subs/get-saved-item-id)
(def get-deleted-item-id   update.subs/get-deleted-item-id)
(def get-copy-item-id      update.subs/get-copy-item-id)
(def get-recovered-item-id update.subs/get-recovered-item-id)
