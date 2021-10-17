
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.21
; Description:
; Version: v0.4.8
; Compatibility: x4.3.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-sync.env-handler
  (:require [mid-fruits.candy  :refer [return]]
            [mid-fruits.time   :as time]
            [x.server-user.api :as user]
            [com.wsscode.pathom3.connect.operation :as pathom.co]))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->request
  ; @param (map) env
  ;  {:request (map)}
  ;
  ; @return (map)
  [{:keys [request]}]
  (return request))

(defn env->user-account-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)
  ;   {:session (map)
  ;    {:user-account/id (string)}}}
  ;
  ; @return (string)
  [{:keys [request]}]
  (let [session (user/request->session request)]
       (get session :user-account/id)))

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
  (pathom.co/params env))

(defn env->param
  ; @param (map) env
  ; @param (keyword) param-id
  ;
  ; @return (*)
  [env param-id]
  (let [params (env->params env)]
       (get params param-id)))

(defn env->modify-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;
  ; @return (map)
  ;  {:modified-at (object)
  ;   :modified-by (map)}
  [env]
  (let [account-id   (env->user-account-id env)
        user-account {:user-account/id account-id}
        timestamp    (time/timestamp)]
       {:modified-at timestamp
        :modified-by user-account}))

(defn env->create-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;
  ; @return (map)
  ;  {:created-at (string)
  ;   :created-by (map)}
  [env]
  (let [account-id   (env->user-account-id env)
        user-account {:user-account/id account-id}
        timestamp    (time/timestamp)]
       {:created-at timestamp
        :created-by user-account}))

(defn env->delete-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;
  ; @return (map)
  ;  {:deleted-at (string)
  ;   :deleted-by (map)}
  [env]
  (let [account-id   (env->user-account-id env)
        user-account {:user-account/id account-id}
        timestamp    (time/timestamp)]
       {:deleted-at timestamp
        :deleted-by user-account}))

(defn env->upload-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;
  ; @return (map)
  ;  {:uploaded-at (string)
  ;   :uploaded-by (map)}
  [env]
  (let [account-id   (env->user-account-id env)
        user-account {:user-account/id account-id}
        timestamp    (time/timestamp)]
       {:uploaded-at timestamp
        :uploaded-by user-account}))
