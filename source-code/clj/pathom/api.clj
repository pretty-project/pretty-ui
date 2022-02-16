
(ns pathom.api
    (:require [pathom.env       :as env]
              [pathom.query     :as query]
              [pathom.register  :as register]
              [pathom.validator :as validator]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; pathom.env
(def env->request env/env->request)
(def env->params  env/env->params)
(def env->param   env/env->param)
(def env<-param   env/env<-param)

; pathom.query
(def process-query!   query/process-query!)
(def process-request! query/process-request!)

; pathom.register
(def reg-handler!  register/reg-handler!)
(def reg-handlers! register/reg-handlers!)

; pathom.validator
(def validate-data        validator/validate-data)
(def clean-validated-data validator/clean-validated-data)
(def data-valid?          validator/data-valid?)
(def data-invalid?        validator/data-invalid?)
