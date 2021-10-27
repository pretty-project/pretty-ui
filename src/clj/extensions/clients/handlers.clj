
(ns extensions.clients.handlers
    (:require
      [mongo-db.api :as mongo-db]
      [x.server-core.api :as a]
      [pathom.api   :as pathom]
      [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))


(def collection-name "clients")

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
;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defresolver get-clients
             ; @param (map) env
             ; @param (?) ?
             ;
             ; @return (map)
             ;  {:clients/get-clients (map)
             ;    {:document-count (integer)
             ;     :documents (maps in vector)}}
             [env _]
             {:clients/get-clients
               (let [search-props (pathom/env->params env)
                     pipeline     (search-props->search-pipeline search-props)]
                    {:documents      (mongo-db/get-documents-by-pipeline   collection-name pipeline)
                     :document-count (mongo-db/count-documents-by-pipeline collection-name pipeline)})})

(defresolver get-client
             ; @param (map) env
             ; @param (?) ?
             ;
             ; @return (map)
             ;  {:clients/get-client (map)}
             [env {:keys [client/id]}]
             {:clients/get-client (mongo-db/get-document-by-id collection-name id)})

;; ----------------------------------------------------------------------------
;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation save-client! [client]
             {::pco/op-name 'clients/save-client!}
             (mongo-db/add-document! collection-name client))

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
