
(ns extensions.clients.handlers
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.time   :as time]
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
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [request]} client-props]
  (let [timestamp (time/timestamp-object)
        user-link (user/request->user-link request)]
       (merge (param client-props)
              {:client/added-at    timestamp
               :client/added-by    user-link
               :client/modified-at timestamp
               :client/modified-by user-link})))



;; ----------------------------------------------------------------------------
;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; -- Pipelines ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->search-props
  [env]
  (let [resolver-props        (pathom/env->params env)
        downloaded-item-count (get resolver-props :downloaded-item-count)
        search-term           (get resolver-props :search-term)
        order-by              (get resolver-props :order-by)]
       {:max-count      10
        :skip           downloaded-item-count
        :search-pattern [[:clients/full-name search-term] [:clients/email-address search-term]]
        :sort-pattern   (case order-by :by-name-ascending  [[:client/first-name  1] [:client/last-name  1]]
                                       :by-name-descending [[:client/first-name -1] [:client/last-name -1]]
                                       :by-date-ascending  [[:client/modified-at  1]]
                                       :by-date-descending [[:client/modified-at -1]])}))

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

(defresolver get-client-items
             ; @param (map) env
             ;  {}
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:clients/get-client-items (map)
             ;    {:document-count (integer)
             ;     :documents (maps in vector)}}
             [env _]
             {:clients/get-client-items
              (let [resolver-props  (pathom/env->params env)
                    search-props    (env->search-props  env)
                    search-pipeline (search-props->search-pipeline search-props)
                    count-pipeline  (search-props->count-pipeline  search-props)]
                   (println (str resolver-props))
                    ; A keresési feltételeknek megfelelő dokumentumok rendezve, skip-elve és limit-elve
                   {:documents      (mongo-db/get-documents-by-pipeline   collection-name search-pipeline)
                    ; A keresési feltételeknek megfelelő dokumentumok száma
                    :document-count (mongo-db/count-documents-by-pipeline collection-name count-pipeline)})})

(defresolver get-client-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;  {:client/id (string)}
             ;
             ; @return (map)
             ;  {:clients/get-client-item (map)}
             [env {:keys [client/id]}]
             {::pco/output [:client/id
                            :client/added-at
                            :client/address
                            :client/city
                            :client/country
                            :client/email-address
                            :client/first-name
                            :client/last-name
                            :client/modified-at
                            :client/phone-number
                            :client/vat-no
                            :client/zip-code]}
             (mongo-db/get-document-by-id collection-name id))

;; ----------------------------------------------------------------------------
;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation save-client-item! [env client-props]
             {::pco/op-name 'clients/save-client-item!}
             (let [client-props (a/sub-prot env client-props client-props-prototype)]
                  (mongo-db/add-document! collection-name client-props)))

(defmutation add-client-item! [client]
             {::pco/op-name 'clients/add-client-item!}
             (mongo-db/add-document! collection-name client))

(defmutation delete-client-item! [{:keys [client-id]}]
             {::pco/op-name 'clients/delete-client-item!}
             (mongo-db/remove-document! collection-name client-id))

(defmutation duplicate-client-item! [{:keys [client-id]}]
             {::pco/op-name 'clients/duplicate-client-item!}
             (mongo-db/duplicate-document! collection-name client-id))

;; ----------------------------------------------------------------------------
;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
(def HANDLERS [get-client-item
               get-client-items

               save-client-item!
               add-client-item!
               delete-client-item!
               duplicate-client-item!])

(pathom/reg-handlers! :clients HANDLERS)
