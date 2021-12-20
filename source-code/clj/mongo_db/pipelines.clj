
(ns mongo-db.pipelines
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.vector  :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filter-pattern->query-pipeline
      ; @param (map) filter-pattern
      ;  {:or (vectors in vector)(opt)
      ;    [[(namespaced-keyword) filter-key
      ;      (*) filter-value]
      ;     ...]
      ;   :and (vectors in vector)(opt)
      ;    [[(namespaced-keyword) filter-key
      ;      (*) filter-value]
      ;     ...]}
      ;
      ; @example
      ;  (mongo-db/filter-pattern->query-pipeline {:or [[:my-namespace/archived? true] [...]]})
      ;  =>
      ;  {"$or" [{"my-namespace/archived?" true} {...}]}
      ;
      ; @return (maps in vector)
      [filter-pattern]
      (reduce-kv (fn [query operator expressions]
                     (let [operator (case operator :and "$and" :or "$or")]
                          (assoc query operator
                                 (reduce (fn [expressions [filter-key filter-value]]
                                             (let [str-filter-key (keyword/to-string filter-key)]
                                                  (vector/conj-item expressions {str-filter-key filter-value})))
                                         (param [])
                                         (param expressions)))))
                 (param {})
                 (param filter-pattern)))

(defn search-pattern->query-pipeline
      ; @param (map) search-pattern
      ;  {:or (vectors in vector)(opt)
      ;    [[(namespaced keyword) search-key
      ;      (string) search-term]
      ;     ...]
      ;   :and (vectors in vector)(opt)
      ;    [[(namespaced keyword) search-key
      ;      (string) search-term]
      ;     ...]}
      ;
      ; @example
      ;  (mongo-db/search-pattern->query-pipeline {:or [[:my-namespace/full-name "Xyz"] [...]]})
      ;  =>
      ;  {"$or" [{"my-namespace/full-name" {"$regex" "Xyz" "$options" "i"}} {...}]}
      ;
      ; @return (maps in vector)
      [search-pattern]
      (reduce-kv (fn [query operator expressions]
                     (let [operator (case operator :and "$and" :or "$or")]
                          (assoc query operator
                                 (reduce (fn [expressions [search-key search-term]]
                                             (let [str-search-key (keyword/to-string search-key)]
                                                  (vector/conj-item expressions {str-search-key {"$regex" search-term "$options" "i"}})))
                                         (param [])
                                         (param expressions)))))
                (param {})
                (param search-pattern)))

(defn sort-pattern->sort-pipeline
      ; @param (vectors in vector) sort-pattern
      ;  [[(namespaced keyword) sort-key
      ;    (integer) sort-direction]]
      ;
      ; @example
      ;  (mongo-db/sort-pattern->sort-pipeline [[:fruit/apple -1] [...]])
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
