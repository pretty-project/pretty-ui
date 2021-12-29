
(ns pathom.api
    (:require [pathom.env      :as env]
              [pathom.query    :as query]
              [pathom.register :as register]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; pathom.env
(def environment-register env/environment-register)
(def env->request         env/env->request)
(def env->params          env/env->params)
(def env->param           env/env->param)

; pathom.query
(def request->query   query/request->query)
(def process-query!   query/process-query!)
(def process-request! query/process-request!)

; pathom.register
(def ENVIRONMENT   register/ENVIRONMENT)
(def reg-handler!  register/reg-handler!)
(def reg-handlers! register/reg-handlers!)
