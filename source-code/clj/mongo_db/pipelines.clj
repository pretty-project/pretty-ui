
(ns mongo-db.pipelines
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.loop    :refer [reduce+first?]]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name pipeline
;
; @name stage
;
; @name operation
;



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filter-pattern->filter-query
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
  ;  (mongo-db/filter-pattern->filter-query {:or [[:my-namespace/my-key true] [...]]})
  ;  =>
  ;  {"$or" [{"my-namespace/my-key" true} {...}]}
  ;
  ; @return (maps in vector)
  [filter-pattern]
  (reduce-kv (fn [query operator expressions]
                 (let [operator (case operator :and "$and" :or "$or")]
                      (assoc query operator
                             (vec (reduce (fn [expressions [filter-key filter-value]]
                                              (let [str-filter-key (keyword/to-string filter-key)]
                                                   (conj expressions {str-filter-key filter-value})))
                                          [] expressions)))))
             {} filter-pattern))

(defn search-pattern->search-query
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
  ;  (mongo-db/search-pattern->search-query {:or [[:my-namespace/my-key "Xyz"] [...]]})
  ;  =>
  ;  {"$or" [{"my-namespace/my-key" {"$regex" "Xyz" "$options" "i"}} {...}]}
  ;
  ; @return (map)
  ;  {:and (maps in vector)
  ;   :or (maps in vector)}
  [search-pattern]
  (reduce-kv (fn [query operator expressions]
                 (let [operator (case operator :and "$and" :or "$or")]
                      (assoc query operator
                             (vec (reduce (fn [expressions [search-key search-term]]
                                              (let [str-search-key (keyword/to-string search-key)]
                                                   (conj expressions {str-search-key {"$regex" search-term "$options" "i"}})))
                                          [] expressions)))))
            {} search-pattern))

(defn sort-pattern->sort-query
  ; @param (vectors in vector) sort-pattern
  ;  [[(namespaced keyword) sort-key
  ;    (integer) sort-direction]]
  ;
  ; @example
  ;  (mongo-db/sort-pattern->sort-query [[:my-namespace/my-key -1] [...]])
  ;  =>
  ;  {"my-namespace/my-key" -1 ...}
  ;
  ; @return (map)
  [sort-pattern]
  (reduce (fn [result [sort-key sort-direction]]
              (let [sort-key (keyword/to-string sort-key)]
                   (assoc result sort-key sort-direction)))
          {} sort-pattern))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-pattern->field-operation
  ; @param (vector) field-pattern
  ;
  ; @example
  ;  (mongo-db/field-pattern->field-operation [:my-namespace/name [:my-namespace/first-name :my-namespace/last-name]])
  ;  =>
  ;  {"$addFields" {"my-namespace/name" {"$concat" ["$my-namespace/first-name" " " "$my-namespace/last-name"]}}}
  ;
  ; @return (map)
  ;  {"$addFields" (map)}
  [[key keys :as field-pattern]]
  {"$addFields" {(keyword/to-string key)
                 {"$concat" (vec (reduce+first? #(if %3 (conj   %1      (str "$" (keyword/to-string %2)))
                                                        (concat %1 [" " (str "$" (keyword/to-string %2))]))
                                                [] keys))}}})
