
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-browser.api
    (:require [server-plugins.item-browser.browser-handler.effects]
              [server-plugins.item-browser.route-handler.effects]
              [server-plugins.item-browser.transfer-handler.effects]
              [server-plugins.item-browser.download-handler.engine :as download-handler.engine]
              [server-plugins.item-browser.update-handler.engine   :as update-handler.engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; server-plugins.item-browser.download-handler.engine
(def env->item-links      download-handler.engine/env->item-links)
(def env->sort-pattern    download-handler.engine/env->sort-pattern)
(def env->search-pattern  download-handler.engine/env->search-pattern)
(def env->pipeline-props  download-handler.engine/env->pipeline-props)
(def env->get-pipeline    download-handler.engine/env->get-pipeline)
(def env->count-pipeline  download-handler.engine/env->count-pipeline)

; server-plugins.item-browser.update-handler.engine
(def item->path           update-handler.engine/item->path)
(def item->parent-link    update-handler.engine/item->parent-link)
(def item->parent-id      update-handler.engine/item->parent-id)
(def item-id->path        update-handler.engine/item-id->path)
(def item-id->parent-link update-handler.engine/item-id->parent-link)
(def item-id->parent-id   update-handler.engine/item-id->parent-id)
