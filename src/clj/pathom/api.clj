
(ns pathom.api
    (:require [pathom.env       :as env]
              [pathom.universal :as universal]
              [pathom.query     :as query]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; pathom.env
(def environment-register env/environment-register)
(def env->request         env/env->request)
(def env->params          env/env->params)
(def env->param           env/env->param)

; pathom.universal

; pathom.query
(def read-query     query/read-query)
(def request->query query/request->query)
(def process-query! query/process-query!)
