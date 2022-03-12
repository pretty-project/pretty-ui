
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.api
    (:require [server-plugins.item-lister.lister-handler.effects]
              [server-plugins.item-lister.route-handler.effects]
              [server-plugins.item-lister.transfer-handler.effects]
              [server-plugins.item-lister.download-handler.engine :as download-handler.engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; server-plugins.item-lister.download-handler.engine
(def env->sort-pattern   download-handler.engine/env->sort-pattern)
(def env->search-pattern download-handler.engine/env->search-pattern)
(def env->pipeline-props download-handler.engine/env->pipeline-props)
(def env->get-pipeline   download-handler.engine/env->get-pipeline)
(def env->count-pipeline download-handler.engine/env->count-pipeline)
