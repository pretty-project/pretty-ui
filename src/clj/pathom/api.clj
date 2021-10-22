
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
(def get-documents-by-pipeline universal/get-documents-by-pipeline)
(def get-documents-by-id       universal/get-document-by-id)
(def update-document!          universal/update-document!)
(def remove-document!          universal/remove-document!)
(def duplicate-document!       universal/duplicate-document!)
(def reorder-documents!        universal/reorder-documents!)

; pathom.query
(def read-query     query/read-query)
(def request->query query/request->query)
(def process-query! query/process-query!)
