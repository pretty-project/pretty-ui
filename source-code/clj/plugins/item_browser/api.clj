
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.api
    (:require [plugins.item-browser.engine.effects]
              [plugins.item-browser.routes.effects]
              [plugins.item-browser.transfer.effects]
              [plugins.item-browser.download.helpers :as download.helpers]
              [plugins.item-browser.update.helpers   :as update.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-browser.download.helpers
(def env->item-links      download.helpers/env->item-links)
(def env->sort-pattern    download.engine/env->sort-pattern)
(def env->search-pattern  download.helpers/env->search-pattern)
(def env->pipeline-props  download.helpers/env->pipeline-props)
(def env->get-pipeline    download.helpers/env->get-pipeline)
(def env->count-pipeline  download.helpers/env->count-pipeline)

; plugins.item-browser.update.helpers
(def item->path           update.helpers/item->path)
(def item->parent-link    update.helpers/item->parent-link)
(def item->parent-id      update.helpers/item->parent-id)
(def item-id->path        update.helpers/item-id->path)
(def item-id->parent-link update.helpers/item-id->parent-link)
(def item-id->parent-id   update.helpers/item-id->parent-id)
