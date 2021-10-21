
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

(defn environment-register
  ; @param (vector) registry
  ;
  ; @return (map)
  [registry]
  (pathom.ci/register registry))



;; -- Converters --------------------------------------------------------------
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
  ; @param (keyword) param-id
  ;
  ; @return (map)
  [env]
  (pathom.co/params env))

(defn env->param
  ; @param (map) env
  ; @param (keyword) param-id
  ;
  ; @return (*)
  [env param-id]
  (let [params (env->params env)]
       (get params param-id)))

(defn env->search-props
  ; @param (map) env
  ;  {?}
  ;
  ; @example
  ;  (env->search-props {?})
  ;  =>
  ;  {:max-count 20
  ;   :search-key :fruit/name
  ;   :search-term "Apple"
  ;   :sort-pattern
  ;   [[:fruit/name 1] [:fruit/color -1]]
  ;   :skip 40}
  ;
  ; @return (map)
  ;  {:max-count (integer)
  ;   :search-key (namespaced keyword)
  ;   :search-term (string)
  ;   :sort-pattern (vectors in vector)
  ;    [[(namespaced keyword) sort-key
  ;      (integer) sort-direction]]
  ;   :skip (integer)}
  [env]
  (env->params env))
