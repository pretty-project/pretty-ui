
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.api
    (:require [plugins.item-viewer.backup.events]
              [plugins.item-viewer.backup.subs]
              [plugins.item-viewer.body.effects]
              [plugins.item-viewer.body.events]
              [plugins.item-viewer.body.subs]
              [plugins.item-viewer.core.effects]
              [plugins.item-viewer.core.events]
              [plugins.item-viewer.download.effects]
              [plugins.item-viewer.download.events]
              [plugins.item-viewer.download.subs]
              [plugins.item-viewer.footer.effects]
              [plugins.item-viewer.footer.events]
              [plugins.item-viewer.footer.subs]
              [plugins.item-viewer.routes.effects]
              [plugins.item-viewer.transfer.subs]
              [plugins.item-viewer.update.effects]
              [plugins.item-viewer.update.events]
              [plugins.item-viewer.update.subs]
              [plugins.item-viewer.body.views   :as body.views]
              [plugins.item-viewer.core.subs    :as core.subs]
              [plugins.item-viewer.footer.views :as footer.views]
              ;[plugins.item-viewer.header.views :as header.views]
              [plugins.item-viewer.routes.subs  :as routes.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-viewer.body.views
(def body body.views/body)

; plugins.item-viewer.core.subs
(def get-current-item-id core.subs/get-current-item-id)
(def get-current-item    core.subs/get-current-item)
(def viewing-item?       core.subs/viewing-item?)

; plugins.item-viewer.footer.views
(def delete-item-block    footer.views/delete-item-block)
(def duplicate-item-block footer.views/duplicate-item-block)
(def edit-item-block      footer.views/edit-item-block)
(def footer               footer.views/footer)

; plugins.item-viewer.header.views
;(def header header.views/header)

; plugins.item-viewer.routes.subs
(def get-item-route routes.subs/get-item-route)
