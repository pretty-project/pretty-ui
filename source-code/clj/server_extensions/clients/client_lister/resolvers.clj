
(ns server-extensions.clients.client-lister.resolvers
    (:require [mid-fruits.candy     :refer [param return]]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [x.server-locales.api :as locales]
              [x.server-user.api    :as user]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [server-plugins.item-lister.api        :as item-lister]))



;; -- Pipelines ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->name-field-pattern
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env]
  (let [request           (pathom/env->request env)
        selected-language (user/request->user-settings-item request :selected-language)
        name-order        (get locales/NAME-ORDERS selected-language)]
       ; A :client/first-name és :client/last-name tulajdonságok sorrendjéhez a felhasználó által
       ; kiválaszott nyelv szerinti sorrendet alkalmazza.
       (case name-order :reversed {:client/name {:$concat [:$client/last-name  " " :$client/first-name]}}
                                  {:client/name {:$concat [:$client/first-name " " :$client/last-name]}})))

(defn env->get-pipeline
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env]
  (let [name-field-pattern (env->name-field-pattern env)
        env (pathom/env<-param env :field-pattern name-field-pattern)]
       (item-lister/env->get-pipeline env :clients :client)))

(defn env->count-pipeline
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env]
  (let [name-field-pattern (env->name-field-pattern env)
        env (pathom/env<-param env :field-pattern name-field-pattern)]
       (item-lister/env->count-pipeline env :clients :client)))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-client-items-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:document-count (integer)
  ;   :documents (namespaced maps in vector)}}
  [env _]
  (let [get-pipeline   (env->get-pipeline   env)
        count-pipeline (env->count-pipeline env)]
       {:documents      (mongo-db/get-documents-by-pipeline   "clients" get-pipeline)
        :document-count (mongo-db/count-documents-by-pipeline "clients" count-pipeline)}))

(defresolver get-client-items
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:clients/get-client-items (map)
             ;    {:document-count (integer)
             ;     :documents (namespaced maps in vector)}}
             [env resolver-props]
             {:clients/get-client-items (get-client-items-f env resolver-props)})



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-client-items])

(pathom/reg-handlers! ::handlers HANDLERS)
