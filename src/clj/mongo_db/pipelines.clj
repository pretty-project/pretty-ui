
(ns mongo-db.pipelines
    (:require
      [mid-fruits.vector :as vector]
      [mid-fruits.candy :refer [param return]]
      [mid-fruits.keyword :as keyword]))

(defn- search-pattern->pipeline-query
       ; @param (vectors in vector) search-pattern
       ;  [[(namespaced keyword) search-key
       ;    (string) search-term]]
       ;
       ; @return (map)
       ;  {"$or" (maps in vector)}
       [search-pattern]
       {"$or" (reduce (fn [query [search-key search-term]]
                          (vector/conj-item query {search-key {"$regex" search-term "$options" "i"}}))
                      (param [])
                      (param search-pattern))})

(defn- sort-pattern->pipeline-sort
       ; @param (vectors in vector) sort-pattern
       ;  [[(namespaced keyword) sort-key
       ;    (integer) sort-direction]]
       ;
       ; @example
       ;  (sort-pattern->pipeline-sort [[:fruit/apple -1] [...]])
       ;  =>
       ;  [["fruit/apple" -1] [...]]
       ;
       ; @return (map)
       ;  [[(string) sort-key
       ;    (integer) sort-direction]]
       [sort-pattern]
       (reduce (fn [sort [sort-key sort-direction]]
                   (let [sort-key (keyword/to-string sort-key)]
                        (vector/conj-item sort [sort-key sort-direction])))
               (param [])
               (param sort-pattern)))


