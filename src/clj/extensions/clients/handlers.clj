
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
           (println [{"$project" {"clients/full-name" {"$concat" ["$clients/first-name" " " "$clients/last-name"]}}}
                     {"$match" query}
                     {"$sort"  sort}
                     {"$skip"  skip}
                     {"$limit" max-count}])
           [{"$addFields" {"clients/full-name" {"$concat" ["$client/first-name" " " "$client/last-name"]}}}]))
            ;{"$match" query}]))
            ;{"$sort"  sort}
            ;{"$skip"  skip}
            ;{"$limit" max-count}]))

;This needs tweaking, something is not okay.


;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-clients-fn [env query]
      (let [search-props (pathom/env->params env)
            pipeline     (search-props->pipeline search-props)]
        (mongo-db/get-documents-by-pipeline-and-count collection-name pipeline)))

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
              (get-clients-fn env nil)})

(defresolver get-client
             ; @param (map) env
             ; @param (?) ?
             ;
             ; @return (map)
             ;  {:clients/get-client (map)
             ;    (map)}}
             [env {:keys [client/id]}]
             {:clients/get-client (mongo-db/get-document-by-id collection-name id)})


; @constant (vector)
(def HANDLERS [get-client
               get-clients])

(pathom/reg-handlers! HANDLERS)
