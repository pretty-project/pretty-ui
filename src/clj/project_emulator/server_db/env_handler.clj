
; WARNING! THIS IS AN OUTDATED VERSION OF A MONO-TEMPLATE FILE!



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns project-emulator.server-db.env-handler
    (:require [com.wsscode.pathom3.connect.operation :as pco]
              [mid-fruits.candy                      :refer [return]]
              [x.server-user.api                     :as user]))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->request
  ; @param (map) env
  ;  {:request (map)}
  ;
  ; @return (map)
  [{:keys [request]}]
  (return request))

(defn env->user-roles
  ; @param (map) env
  ;  {:request (map)
  ;   {:session (map)
  ;    {:user-account/roles (vector)}}}
  ;
  ; @return (map)
  [{:keys [request]}]
  (let [session (user/request->session request)]
       (get session :user-account/roles)))

(defn env->params
  ; @param (map) env
  ; @param (keyword) param-id
  ;
  ; @return (map)
  [env]
  (pco/params env))

(defn env->param
  ; @param (map) env
  ; @param (keyword) param-id
  ;
  ; @return (*)
  [env param-id]
  (let [params (env->params env)]
       (get params param-id)))
