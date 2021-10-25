
(ns clients.mongo-db.mongo-pipelines
    (:require
      [mid-fruits.vector  :as vector]
      [mid-fruits.candy   :refer [param return]]
      [mid-fruits.keyword :as keyword]
      [mongo-db.api       :as mongo-db]))

(defn search-props->pipeline
      ; Example: (search-props->pipeline
      ;             {:skip 0
      ; :max-count 20
      ; :search-pattern [[ski""]]}
      [{:keys [max-count skip search-pattern sort-pattern] :as search-props}]
      (let [query      (mongo-db/search-pattern->pipeline-query search-pattern)
            sort       (mongo-db/sort-pattern->pipeline-sort    sort-pattern)]
           [{"$project" {"clients/full-name" {"$concat" ["$clients/first-name" " " "$clients/last-name"]}}}
            {"$match" query}
            {"$sort"  sort}
            {"$skip"  skip}
            {"$limit" max-count}]))
