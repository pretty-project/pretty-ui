
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.21
; Description:
; Version: v0.4.8
; Compatibility: x4.3.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.env
    (:require [mid-fruits.candy   :refer [return]]
              [mid-fruits.time    :as time]
              [server-fruits.http :as http]
              [com.wsscode.pathom3.connect.indexes   :as pathom.ci]
              [com.wsscode.pathom3.connect.operation :as pathom.co]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->request
  ; @param (map) env
  ;  {:request (map)}
  ;
  ; @return (map)
  [{:keys [request]}]
  (return request))

(defn env->params
  ; @param (map) env
  ;  {:request (map)
  ;   {:params (map)}}
  ; @param (keyword) param-id
  ;
  ; @return (map)
  [env]
  (pathom.co/params env))

(defn env->param
  ; @param (map) env
  ;  {:request (map)
  ;   {:params (map)}}
  ; @param (keyword) param-id
  ;
  ; @return (*)
  [env param-id]
  (let [params (env->params env)]
       (get params param-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn environment-register
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) registry
  ;
  ; @return (map)
  [registry]
  (pathom.ci/register registry))
