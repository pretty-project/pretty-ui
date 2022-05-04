
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.helpers
    (:require [pathom.api              :as pathom]
              [plugins.item-lister.api :as item-lister]
              [x.server-locales.api    :as locales]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->name-field-pattern
  [{:keys [request]}]
  ; A :client/first-name és :client/last-name tulajdonságok sorrendjéhez a felhasználó által
  ; kiválaszott nyelv szerinti sorrendet alkalmazza.
  (let [name-order (locales/request->name-order request)]
       (case name-order :reversed {:client/name {:$concat [:$client/last-name  " " :$client/first-name]}}
                                  {:client/name {:$concat [:$client/first-name " " :$client/last-name]}})))

(defn env->get-pipeline
  [env]
  ; XXX#7601
  ; A :client/name virtuális mezőt szükséges hozzáadni a dokumentumokhoz a keresés és rendezés előtt!
  (let [name-field-pattern (env->name-field-pattern env)
        env                (pathom/env<-param env :field-pattern name-field-pattern)]
       (item-lister/env->get-pipeline env :clients.client-lister)))

(defn env->count-pipeline
  [env]
  (let [name-field-pattern (env->name-field-pattern env)
        env (pathom/env<-param env :field-pattern name-field-pattern)]
       (item-lister/env->count-pipeline env :clients.client-lister)))
