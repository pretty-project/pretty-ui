
(ns mongo-db.pipelines
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]
              [mid-fruits.vector  :as vector]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name pipeline
;  TODO ...
;
; @name stage
;  TODO ...
;
; @name operation
;  TODO ...



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
  ;  (mongo-db/filter-pattern->filter-query {:or  [[:my-namespace/my-key   false]
  ;                                                [:my-namespace/my-key   nil]]
  ;                                          :and [[:my-namespace/your-key true]]})
  ;  =>
  ;  {"$or"  [{"my-namespace/my-key"   false}
  ;           {"my-namespace/my-key"   nil}]
  ;   "$and" [{"my-namespace/your-key" true}]}
  ;
  ; @return (maps in vector)
  [filter-pattern]
  (letfn [(f [[filter-key filter-value]]
             {(keyword/to-string filter-key) filter-value})]
         (map/->kv filter-pattern #(case           % :and "$and" :or "$or")
                                  #(vector/->items % f))))

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
  ;  (mongo-db/search-pattern->search-query {:or [[:my-namespace/my-key   "Xyz"]
  ;                                               [:my-namespace/your-key "Xyz"]]})
  ;  =>
  ;  {"$or" [{"my-namespace/my-key"   {"$regex" "Xyz" "$options" "i"}}
  ;          {"my-namespace/your-key" {"$regex" "Xyz" "$options" "i"}}]}
  ;
  ; @return (map)
  ;  {:and (maps in vector)
  ;   :or (maps in vector)}
  [search-pattern]
  (letfn [(f [[search-key search-term]]
             {(keyword/to-string search-key) {"$regex" search-term "$options" "i"}})]
         (map/->kv search-pattern #(case           % :and "$and" :or "$or")
                                  #(vector/->items % f))))

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
  (letfn [(f [o [sort-key sort-direction]]
             (assoc o (keyword/to-string sort-key) sort-direction))]
         (reduce f {} sort-pattern)))



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
  (letfn [(f [o x] (if (empty? o) (conj o     (str "$" (keyword/to-string x)))
                                  (conj o " " (str "$" (keyword/to-string x)))))]
         {"$addFields" {(keyword/to-string key) {"$concat" (reduce f [] keys)}}}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-pipeline
  ; @param (map) pipeline-props
  ;  {:filter-pattern (map)
  ;   :max-count (integer)
  ;   :search-pattern (map)
  ;   :skip (integer)
  ;   :sort-pattern (vectors in vector)}
  ;
  ; @usage
  ;  (mongo-db/get-pipeline {:filter-pattern {:or [[:my-namespace/my-key   false]
  ;                                                [:my-namespace/my-key   nil]]}
  ;                          :search-pattern {:or [[:my-namespace/my-key   "Xyz"]
  ;                                                [:my-namespace/your-key "Xyz"]]}
  ;                          :sort-pattern [:my-namespace/my-key -1]
  ;                          :max-count 20
  ;                          :skip      40})
  ;
  ; @return (maps in vector)
  [{:keys [filter-pattern max-count search-pattern skip sort-pattern]}]
  (let [filter-query (filter-pattern->filter-query filter-pattern)
        search-query (search-pattern->search-query search-pattern)
        sort         (sort-pattern->sort-query     sort-pattern)]
       [{"$match" {"$and" [filter-query search-query]}}
        {"$sort"  sort}
        {"$skip"  skip}
        {"$limit" max-count}]))

(defn count-pipeline
  ; @param (map) pipeline-props
  ;  {:filter-pattern (map)
  ;   :search-pattern (map)}
  ;
  ; @usage
  ;  (mongo-db/count-pipeline {...})
  ;
  ; @return (maps in vector)
  [{:keys [filter-pattern search-pattern]}]
  (let [filter-query (filter-pattern->filter-query filter-pattern)
        search-query (search-pattern->search-query search-pattern)]
       [{"$match" {"$and" [filter-query search-query]}}]))
