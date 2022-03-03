
(ns pathom.env
    (:require [com.wsscode.pathom3.connect.indexes   :as pathom.ci]
              [com.wsscode.pathom3.connect.operation :as pathom.co]
              [mid-fruits.candy                      :refer [param return]]))



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
  ;
  ; @return (map)
  [env]
  (pathom.co/params env))

(defn env->param
  ; @param (map) env
  ; @param (keyword) param-key
  ;
  ; @return (*)
  [env param-key]
  (let [params (env->params env)]
       (get params param-key)))

(defn env<-param
  ; @param (map) env
  ; @param (keyword) param-key
  ; @param (*) param-value
  ;
  ; @return (map)
  [env param-key param-value]
  ; https://github.com/wilkerlucio/pathom3/blob/main/src/main/com/wsscode/pathom3/connect/operation.cljc
  (assoc-in env [:com.wsscode.pathom3.connect.planner/node :com.wsscode.pathom3.connect.planner/params param-key]
            param-value))



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
