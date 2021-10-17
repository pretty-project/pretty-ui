
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.16
; Description:
; Version: v0.4.8
; Compatibility: x4.3.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-sync.api
    (:require [x.server-sync.env-handler   :as env-handler]
              [x.server-sync.query-handler :as query-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-sync.env-handler
(def env->request         env-handler/env->request)
(def env->user-account-id env-handler/env->user-account-id)
(def env->user-roles      env-handler/env->user-roles)
(def env->params          env-handler/env->params)
(def env->param           env-handler/env->param)
(def env->modify-props    env-handler/env->modify-props)
(def env->create-props    env-handler/env->create-props)
(def env->delete-props    env-handler/env->delete-props)
(def env->upload-props    env-handler/env->upload-props)

; x.server-sync.query-handler
(def read-query           query-handler/read-query)
(def append-to-query      query-handler/append-to-query)
(def concat-queries       query-handler/concat-queries)
(def query-action         query-handler/query-action)
(def id->placeholder query-handler/id->placeholder)
(def environment-register query-handler/environment-register)
(def request->query       query-handler/request->query)
(def process-query!       query-handler/process-query!)
