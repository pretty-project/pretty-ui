
(ns extensions.clients.handlers
    (:require
      [mongo-db.api :as mongo-db]
      [x.server-core.api :as a]
      [pathom.api   :as pathom]
      [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))


(def collection-name "clients")

;; -- Pipelines ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn search-props->pipeline
      ; Example: (search-props->pipeline
      ;             {:skip 0
      ;              :max-count 20
      ;              :search-pattern [[:clients/first-name "sad"
      ;              :sort-pattern   [[:clients/first-name 1]]}
      [{:keys [max-count skip search-pattern sort-pattern] :as search-props}]
      (let [query      (mongo-db/search-pattern->pipeline-query search-pattern)
            sort       (mongo-db/sort-pattern->pipeline-sort    sort-pattern)]
           [{"$addFields" {"clients/full-name" {"$concat" ["$client/first-name" " " "$client/last-name"]}}}
            {"$match" query}
            {"$sort"  sort}
            {"$skip"  skip}
            {"$limit" max-count}]))

;This needs tweaking, something is not okay.

;; ----------------------------------------------------------------------------
;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defresolver get-clients
             ; @param (map) env
             ; @param (?) ?
             ;
             ; @return (map)
             ;  {:clients/get-clients (map)
             ;    {:item-count (integer)
             ;     :items (maps in vector)}}
             [env _]
             {:clients/get-clients
               (let [search-props (pathom/env->params env)
                     pipeline     (search-props->pipeline search-props)]
                    {:documents      (mongo-db/get-documents-by-pipeline    collection-name pipeline)
                     :document-count (mongo-db/count-documents-by-pipeline  collection-name pipeline)})})

(defresolver get-client
             ; @param (map) env
             ; @param (?) ?
             ;
             ; @return (map)
             ;  {:clients/get-client (map)
             ;    (map)}}
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

(defmutation delete-client! [{:keys [id]}]
             {::pco/op-name 'clients/delete-client!}
             (mongo-db/remove-document! collection-name id))

(defmutation duplicate-client! [{:keys [id]}]
             {::pco/op-name 'clients/duplicate-client!}
             (mongo-db/duplicate-document! collection-name id))

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
