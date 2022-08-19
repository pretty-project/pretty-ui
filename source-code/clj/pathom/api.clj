
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.api
    (:require [pathom.debug.resolvers]
              [pathom.env.helpers           :as env.helpers]
              [pathom.query.side-effects    :as query.side-effects]
              [pathom.register.side-effects :as register.side-effects]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; pathom.env.helpers
(def env->request         env.helpers/env->request)
(def env->resolver-params env.helpers/env->resolver-params)
(def env->mutation-params env.helpers/env->mutation-params)
(def env->params          env.helpers/env->params)
(def env->param           env.helpers/env->param)
(def env<-param           env.helpers/env<-param)

; pathom.query.side-effects
(def process-query!   query.side-effects/process-query!)
(def process-request! query.side-effects/process-request!)

; pathom.register.side-effects
(def reg-handler!  register.side-effects/reg-handler!)
(def reg-handlers! register.side-effects/reg-handlers!)
