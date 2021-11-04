
(ns extensions.clients.handlers
    (:require [mid-fruits.candy  :refer [param return]]
              [mongo-db.api      :as mongo-db]
              [pathom.api        :as pathom]
              [x.server-core.api :as a]
              [x.server-user.api :as user]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def collection-name "clients")

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-props-prototype
  [{:keys [request]} client-props]
  (let [add-props (user/request->add-props request)]
       (merge client-props add-props)))

;; ----------------------------------------------------------------------------
;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; -- Pipelines ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn search-props->search-pipeline
      ; Example: (search-props->search-pipeline
      ;             {:skip 0
      ;              :max-count 20
      ;              :search-pattern [[:clients/first-name "sad"]]
      ;              :sort-pattern   [[:clients/first-name 1]]}
      [{:keys [max-count skip search-pattern sort-pattern] :as search-props}]
      (let [query      (mongo-db/search-pattern->pipeline-query search-pattern)
            sort       (mongo-db/sort-pattern->pipeline-sort    sort-pattern)]
           [{"$addFields" {"clients/full-name" {"$concat" ["$client/first-name" " " "$client/last-name"]}}}
            {"$match" query}
            {"$sort"  sort}
            {"$skip"  skip}
            {"$limit" max-count}]))

(defn search-props->count-pipeline
      ; Example: (search-props->count-pipeline
      ;             {:search-pattern [[:clients/first-name "sad"]]}
      [{:keys [search-pattern] :as search-props}]
      (let [query (mongo-db/search-pattern->pipeline-query search-pattern)]
           [{"$addFields" {"clients/full-name" {"$concat" ["$client/first-name" " " "$client/last-name"]}}}
            {"$match" query}]))

;; ----------------------------------------------------------------------------
;; -- Pipelines ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defresolver get-clients
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:clients/get-clients (map)
             ;    {:document-count (integer)
             ;     :documents (maps in vector)}}
             [env _]
             {:clients/get-clients
               (let [search-props    (pathom/env->params            env)
                     search-pipeline (search-props->search-pipeline search-props)
                     count-pipeline  (search-props->count-pipeline  search-props)]
                     ; A keresési feltételeknek megfelelő dokumentumok rendezve, skip-elve és limit-elve
                    {:documents      (mongo-db/get-documents-by-pipeline   collection-name search-pipeline)
                     ; A keresési feltételeknek megfelelő dokumentumok száma
                     :document-count (mongo-db/count-documents-by-pipeline collection-name count-pipeline)})})

(defresolver get-client
             ; @param (map) env
             ; @param (map) resolver-props
             ;  {:client/id (string)}
             ;
             ; @return (map)
             ;  {:clients/get-client (map)}
             [env {:keys [client/id]}]
             {::pco/output [:client/id :client/email-address]}
             (println "sdfdsfsd"))
             ;(mongo-db/get-document-by-id collection-name id))

;; ----------------------------------------------------------------------------
;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation save-client! [env client-props]
             {::pco/op-name 'clients/save-client!}
             (let [client-props (a/sub-prot env client-props client-props-prototype)]
                  (mongo-db/add-document! collection-name client-props)))

(defmutation add-client! [client]
             {::pco/op-name 'clients/add-client!}
             (mongo-db/add-document! collection-name client))

(defmutation delete-client! [{:keys [client-id]}]
             {::pco/op-name 'clients/delete-client!}
             (mongo-db/remove-document! collection-name client-id))

(defmutation duplicate-client! [{:keys [client-id]}]
             {::pco/op-name 'clients/duplicate-client!}
             (mongo-db/duplicate-document! collection-name client-id))

;; ----------------------------------------------------------------------------
;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
(def HANDLERS [get-client
               get-clients

               save-client!
               add-client!
               delete-client!
               duplicate-client!])

(pathom/reg-handlers! :clients HANDLERS)
