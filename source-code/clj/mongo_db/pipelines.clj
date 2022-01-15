
(ns mongo-db.pipelines
    (:require [mid-fruits.candy    :refer [param return]]
              [mid-fruits.keyword  :as keyword]
              [mid-fruits.map      :as map]
              [mid-fruits.vector   :as vector]
              [mongo-db.adaptation :as adaptation]))



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
  ;  {:or (maps in vector)(opt)
  ;   :and (maps in vector)(opt)
  ;
  ; @example
  ;  (mongo-db/filter-pattern->filter-query {:or  [{:namespace/my-key   false}
  ;                                                {:namespace/my-key   nil}]
  ;                                          :and [{:namespace/your-key true}]})
  ;  =>
  ;  {"$or"  [{"namespace/my-key"   false}
  ;           {"namespace/my-key"   nil}]
  ;   "$and" [{"namespace/your-key" true}]}
  ;
  ; @return (maps in vector)
  [filter-pattern]
  (try (map/->kv filter-pattern #(case           % :and "$and" :or "$or")
                                #(vector/->items % adaptation/pipeline-query))
       (catch Exception e (println e))))

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
  ;  (mongo-db/search-pattern->search-query {:or [[:namespace/my-key   "Xyz"]
  ;                                               [:namespace/your-key "Xyz"]]})
  ;  =>
  ;  {"$or" [{"namespace/my-key"   {"$regex" "Xyz" "$options" "i"}}
  ;          {"namespace/your-key" {"$regex" "Xyz" "$options" "i"}}]}
  ;
  ; @return (map)
  ;  {:and (maps in vector)
  ;   :or (maps in vector)}
  [search-pattern]
  (letfn [(f [[search-key search-term]]
             {(keyword/to-string search-key) {"$regex" search-term "$options" "i"}})]
         (try (map/->kv search-pattern #(case           % :and "$and" :or "$or")
                                       #(vector/->items % f))
              (catch Exception e (println e)))))

(defn sort-pattern->sort-query
  ; @param (vectors in vector) sort-pattern
  ;  [[(namespaced keyword) sort-key
  ;    (integer) sort-direction]]
  ;
  ; @example
  ;  (mongo-db/sort-pattern->sort-query [[:namespace/my-key -1] [...]])
  ;  =>
  ;  {"namespace/my-key" -1 ...}
  ;
  ; @return (map)
  [sort-pattern]
  (letfn [(f [o [sort-key sort-direction]]
             (assoc o (keyword/to-string sort-key) sort-direction))]
         (try (reduce f {} sort-pattern)
              (catch Exception e (println e)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-pattern->field-operation
  ; @param (vector) field-pattern
  ;
  ; @example
  ;  (mongo-db/field-pattern->field-operation [:namespace/name [:namespace/first-name :namespace/last-name]])
  ;  =>
  ;  {"$addFields" {"namespace/name" {"$concat" ["$namespace/first-name" " " "$namespace/last-name"]}}}
  ;
  ; @return (map)
  ;  {"$addFields" (map)}
  [[key keys :as field-pattern]]
  (letfn [(f [o x] (if (empty? o) (conj o     (str "$" (keyword/to-string x)))
                                  (conj o " " (str "$" (keyword/to-string x)))))]
         (try {"$addFields" {(keyword/to-string key) {"$concat" (reduce f [] keys)}}}
              (catch Exception e (println e)))))



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
  ;  (mongo-db/get-pipeline {:filter-pattern {:or [{:namespace/my-key   false}
  ;                                                {:namespace/my-key   nil}]}
  ;                          :search-pattern {:or [[:namespace/my-key   "Xyz"]
  ;                                                [:namespace/your-key "Xyz"]]}
  ;                          :sort-pattern [:namespace/my-key -1]
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
