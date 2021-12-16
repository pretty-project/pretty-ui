
(ns server-extensions.clients.handlers
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]
              [mid-fruits.time   :as time]
              [mongo-db.api      :as mongo-db]
              [pathom.api        :as pathom]
              [x.server-core.api :as a]
              [x.server-db.api   :as db]
              [x.server-user.api :as user]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def collection-name "clients")



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- added-client-props-prototype
       ; WARNING! NON-PUBLIC! DO NOT USE!
       [{:keys [request]} client-props]
       (let [timestamp (time/timestamp-object)
             user-link (user/request->user-link request)]
            (merge {:client/added-at    timestamp
                    :client/added-by    user-link}
                   (param client-props)
                   {:client/modified-at timestamp
                    :client/modified-by user-link})))

(defn- updated-client-props-prototype
       ; WARNING! NON-PUBLIC! DO NOT USE!
       [{:keys [request]} client-props]
       (let [timestamp (time/timestamp-object)
             user-link (user/request->user-link request)]
            (merge (param client-props)
                   {:client/modified-at timestamp
                    :client/modified-by user-link})))

(defn- duplicated-client-props-prototype
       ; WARNING! NON-PUBLIC! DO NOT USE!
       [{:keys [request]} client-props]
       (let [timestamp (time/timestamp-object)
             user-link (user/request->user-link request)]
            (merge (param client-props)
                   {:client/added-at    timestamp
                    :client/added-by    user-link
                    :client/modified-at timestamp
                    :client/modified-by user-link})))



;; -- Pipelines ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->search-props
      ; WARNING! NON-PUBLIC! DO NOT USE!
      [env]
      (let [resolver-props        (pathom/env->params env)
            downloaded-item-count (get resolver-props :downloaded-item-count)
            search-term           (get resolver-props :search-term)
            order-by              (get resolver-props :order-by)
            max-count             (get resolver-props :download-limit)]
           {:max-count      max-count
            :skip           downloaded-item-count
            :search-pattern [[:clients/full-name search-term] [:clients/email-address search-term]]
            :sort-pattern   (case order-by :by-name-ascending  [[:client/first-name   1] [:client/last-name  1]]
                                           :by-name-descending [[:client/first-name  -1] [:client/last-name -1]]
                                           :by-date-ascending  [[:client/modified-at  1]]
                                           :by-date-descending [[:client/modified-at -1]])}))

(defn search-props->search-pipeline
      ; WARNING! NON-PUBLIC! DO NOT USE!
      ;
      ; @usage
      ;  (search-props->search-pipeline {:max-count 20
      ;                                  :skip       0
      ;                                  :search-pattern [[:clients/first-name "xyz"]]
      ;                                  :sort-pattern   [[:clients/first-name 1]]}
      [{:keys [max-count skip search-pattern sort-pattern] :as search-props}]
      (let [query      (mongo-db/search-pattern->pipeline-query search-pattern)
            sort       (mongo-db/sort-pattern->pipeline-sort    sort-pattern)]
           [{"$addFields" {"clients/full-name" {"$concat" ["$client/first-name" " " "$client/last-name"]}}}
            {"$match" query}
            {"$sort"  sort}
            {"$skip"  skip}
            {"$limit" max-count}]))

(defn search-props->count-pipeline
      ; WARNING! NON-PUBLIC! DO NOT USE!
      ;
      ; @usage
      ;  (search-props->count-pipeline {:search-pattern [[:clients/first-name "xyz"]]}
      [{:keys [search-pattern] :as search-props}]
      (let [query (mongo-db/search-pattern->pipeline-query search-pattern)]
           [{"$addFields" {"clients/full-name" {"$concat" ["$client/first-name" " " "$client/last-name"]}}}
            {"$match" query}]))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defresolver get-client-suggestions
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ;  {}
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:clients/get-client-suggestions (map)
             ;   {:cities (strings in vector)}}
             [env _]
             {:clients/get-client-suggestions
              (let [all-documents   (mongo-db/get-all-documents :clients)
                    suggestion-keys (pathom/env->param      env :suggestion-keys)]
                   (reduce (fn [a document]
                               (reduce (fn [b suggestion-key]
                                           (let [suggestion-value (db/get-document-value document suggestion-key)]
                                                (if (string/nonempty? suggestion-value)
                                                    (update b suggestion-key vector/conj-item-once suggestion-value)
                                                    (return b))))
                                       (param a)
                                       (param suggestion-keys)))
                           {}
                           (param all-documents)))})

(defresolver get-client-items
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
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
              (let [search-props    (env->search-props  env)
                    search-pipeline (search-props->search-pipeline search-props)
                    count-pipeline  (search-props->count-pipeline  search-props)]
                    ; A keresési feltételeknek megfelelő dokumentumok rendezve, skip-elve és limit-elve
                   {:documents      (mongo-db/get-documents-by-pipeline   collection-name search-pipeline)
                    ; A keresési feltételeknek megfelelő dokumentumok száma
                    :document-count (mongo-db/count-documents-by-pipeline collection-name count-pipeline)})})

(defresolver get-client-item
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
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
                            :client/archived?
                            :client/city
                            :client/colors
                            :client/country
                            :client/email-address
                            :client/favorite?
                            :client/first-name
                            :client/last-name
                            :client/modified-at
                            :client/phone-number
                            :client/vat-no
                            :client/zip-code]}
             (mongo-db/get-document-by-id collection-name id))



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation add-client-item! [env client-props]
             {::pco/op-name 'clients/add-client-item!}
             (mongo-db/add-document! collection-name client-props
                                     {:prototype-f #(a/sub-prot env % added-client-props-prototype)}))

(defmutation undo-delete-client-item! [env client-props]
             {::pco/op-name 'clients/undo-delete-client-item!}
             (mongo-db/add-document! collection-name client-props))

(defmutation save-client-item! [env client-props]
             {::pco/op-name 'clients/save-client-item!}
             (mongo-db/update-document! collection-name client-props
                                        {:prototype-f #(a/sub-prot env % updated-client-props-prototype)}))

(defmutation merge-client-item! [env client-props]
             {::pco/op-name 'clients/merge-client-item!}
             (mongo-db/merge-document! collection-name client-props
                                       {:prototype-f #(a/sub-prot env % updated-client-props-prototype)}))

(defmutation delete-client-item! [{:keys [client-id]}]
             {::pco/op-name 'clients/delete-client-item!}
             (mongo-db/remove-document! collection-name client-id))

(defmutation duplicate-client-item! [env {:keys [client-id]}]
             {::pco/op-name 'clients/duplicate-client-item!}
             (mongo-db/duplicate-document! collection-name client-id
                                           {:prototype-f #(a/sub-prot env % duplicated-client-props-prototype)}))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
(def HANDLERS [get-client-suggestions
               get-client-items
               get-client-item
               add-client-item!
               undo-delete-client-item!
               save-client-item!
               merge-client-item!
               delete-client-item!
               duplicate-client-item!])

(pathom/reg-handlers! :clients HANDLERS)
