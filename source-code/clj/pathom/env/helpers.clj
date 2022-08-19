
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.env.helpers
    (:require [com.wsscode.pathom3.connect.operation :as pathom.co]
              [mid-fruits.candy                      :refer [return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->request
  ; @param (map) env
  ;  {:request (map)}
  ;
  ; @usage
  ;  (pathom/env->request {...})
  ;
  ; @return (map)
  [{:keys [request]}]
  (return request))

(defn env->resolver-params
  ; @param (map) env
  ;
  ; @usage
  ;  (pathom/env->resolver-params {...})
  ;
  ; @return (map)
  [env]
  ; A pathom.co/params függvény csak resolver függvények esetén tér vissza a paraméterekkel.
  (pathom.co/params env))

(defn env->mutation-params
  ; @param (map) env
  ;
  ; @usage
  ;  (pathom/env->mutation-params {...})
  ;
  ; @return (map)
  [env]
  ; - Az egyes mutation függvények env térképének vizsgálata alapján ... a paraméterek mutation függvények
  ;   esetén is kinyerhetők az env térképből.
  ; - A pathom.co/params függvényhez hasonlóan az env->mutation-params függvény is minden esetben egy térképpel tér vissza!
  (or (-> env :com.wsscode.pathom3.connect.planner/graph :com.wsscode.pathom3.connect.planner/mutations first :params)
      (return {})))

(defn env->params
  ; @param (map) env
  ;
  ; @usage
  ;  (pathom/env->params {...})
  ;
  ; @return (map)
  [env]
  ; Az env->resolver-params és env->mutation-params függvény is minden esetben egy térképpel térnek vissza,
  ; ezért ha az első függvény egy üres térképpel tér vissza, megpróbálja a második függvénnyel kinyerni
  ; az env térképből az aktuális mutation/resolver függvény paramétereit.
  (if (-> env env->resolver-params empty? not)
      (-> env env->resolver-params)
      (-> env env->mutation-params)))

(defn env->param
  ; @param (map) env
  ; @param (keyword) param-key
  ;
  ; @usage
  ;  (pathom/env->param {...} :my-param)
  ;
  ; @return (*)
  [env param-key]
  (let [params (env->params env)]
       (param-key params)))

(defn env<-param
  ; @param (map) env
  ; @param (keyword) param-key
  ; @param (*) param-value
  ;
  ; @usage
  ;  (pathom/env<-param {...} :my-param "My value")
  ;
  ; @return (map)
  [env param-key param-value]
  ; https://github.com/wilkerlucio/pathom3/blob/main/src/main/com/wsscode/pathom3/connect/operation.cljc
  (assoc-in env [:com.wsscode.pathom3.connect.planner/node :com.wsscode.pathom3.connect.planner/params param-key]
            param-value))
