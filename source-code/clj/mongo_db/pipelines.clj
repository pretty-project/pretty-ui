
(ns mongo-db.pipelines
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.vector  :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filter-pattern->pipeline-query
      ; @param (vectors in vector) filter-pattern
      ;  [[(namespaced-keyword) filter-key
      ;    (*) filter-value]]
      ;
      ; @example
      ;  (mongo-db/filter-pattern->pipeline-query [[:my-namespace/archived? true] ...])
      ;  =>
      ;  [{"my-namespace/archived?" true} ...]
      ;
      ; @return (maps in vector)
      [filter-pattern]
      (reduce (fn [query [filter-key filter-value]]
                  (let [str-filter-key (keyword/to-string filter-key)]
                       (vector/conj-item query {str-filter-key filter-value})))
              (param [])
              (param filter-pattern)))

(defn search-pattern->pipeline-query
      ; @param (vectors in vector) search-pattern
      ;  [[(namespaced keyword) search-key
      ;    (string) search-term]]
      ;
      ; @example
      ;  (mongo-db/search-pattern->pipeline-query [[:my-namespace/full-name ""] ...])
      ;  =>
      ;  [{"my-namespace/full-name" {"$regex" "" "$options" "i"}} ...]
      ;
      ; @return (maps in vector)
      [search-pattern]
      (reduce (fn [query [search-key search-term]]
                  (let [str-search-key (keyword/to-string search-key)]
                       (vector/conj-item query {str-search-key {"$regex" search-term "$options" "i"}})))
              (param [])
              (param search-pattern)))

(defn sort-pattern->pipeline-sort
      ; @param (vectors in vector) sort-pattern
      ;  [[(namespaced keyword) sort-key
      ;    (integer) sort-direction]]
      ;
      ; @example
      ;  (mongo-db/sort-pattern->pipeline-sort [[:fruit/apple -1] [...]])
      ;  =>
      ;  [["fruit/apple" -1] [...]]
      ;
      ; @return (map)
      [sort-pattern]
      (reduce (fn [sort [sort-key sort-direction]]
                  (let [str-sort-key (keyword/to-string sort-key)]
                       (assoc sort str-sort-key sort-direction)))
              (param {})
              (param sort-pattern)))
