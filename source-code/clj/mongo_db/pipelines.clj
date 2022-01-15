
(ns mongo-db.pipelines
    (:require [mid-fruits.candy    :refer [param return]]
              [mid-fruits.json     :as json]
              [mid-fruits.keyword  :as keyword]
              [mid-fruits.map      :as map]
              [mid-fruits.vector   :as vector]
              [mongo-db.adaptation :as adaptation]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name pipeline
;  An aggregation pipeline can return results for groups of documents.
;
; @name stage
;  Each stage performs an operation on the input documents.
;
; @name operation
;  TODO ...



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-fields-query
  ; @param (map) field-pattern
  ;
  ; @example
  ;  (mongo-db/add-fields-operation {:namespace/name  {:$concat [:$namespace/first-name " " :$namespace/last-name]}
  ;                                  :namespace/total {:$sum     :$namespace/all-result}})
  ;  =>
  ;  {"namespace/name"  {"$concat" ["$namespace/first-name" " " "$namespace/last-name"]}
  ;   "namespace/total" {"$sum"     "$namespace/all-result"}}}
  ;
  ; @return (map)
  [field-pattern]
  ; A kulcsok és értékek függvénye is json/unkeywordize-key, mert szükségetlen a json/unkeywordize-value
  ; függvény által használt biztonsági prefixum alkalmazása.
  (map/->>kv field-pattern json/unkeywordize-key json/unkeywordize-key))

(defn filter-query
  ; @param (map) filter-pattern
  ;  {:or (maps in vector)(opt)
  ;   :and (maps in vector)(opt)
  ;
  ; @example
  ;  (mongo-db/filter-query {:$or  [{:namespace/my-key   false}
  ;                                 {:namespace/my-key   nil}]
  ;                          :$and [{:namespace/your-key true}]})
  ;  =>
  ;  {"$or"  [{"namespace/my-key"   false}
  ;           {"namespace/my-key"   nil}]
  ;   "$and" [{"namespace/your-key" true}]}
  ;
  ; @return (maps in vector)
  [{:keys [$and $or]}]
  (cond-> {} $and (assoc "$and" (vector/->items $and adaptation/pipeline-query))
             $or  (assoc "$or"  (vector/->items $or  adaptation/pipeline-query))))

(defn search-query
  ; @param (map) search-pattern
  ;  {:$and (maps in vector)(opt)
  ;   :$or (maps in vector)(opt)}
  ;
  ; @example
  ;  (mongo-db/search-query {:$or [{:namespace/my-key   "Xyz"}
  ;                                {:namespace/your-key "Xyz"}]})
  ;  =>
  ;  {"$or" [{"namespace/my-key"   {"$regex" "Xyz" "$options" "i"}}
  ;          {"namespace/your-key" {"$regex" "Xyz" "$options" "i"}}]}
  ;
  ; @return (map)
  ;  {"$and" (maps in vector)
  ;   "$or" (maps in vector)}
  [{:keys [$and $or]}]
  (letfn [(adapt-value [v] {"$regex" v "$options" "i"})
          (f [n] (map/->kv n #(json/unkeywordize-key %)
                             #(adapt-value           %)))]
         (cond-> {} $and (assoc "$and" (vector/->items $and f))
                    $or  (assoc "$or"  (vector/->items $or  f)))))

(defn sort-query
  ; @param (map) sort-pattern
  ;
  ; @example
  ;  (mongo-db/sort-query {:namespace/my-key -1 ...})
  ;  =>
  ;  {"namespace/my-key" -1 ...}
  ;
  ; @return (map)
  [sort-pattern]
  (map/->keys sort-pattern json/unkeywordize-keys))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-pipeline
  ; @param (map) pipeline-props
  ;  {:field-pattern (map)(opt)
  ;   :filter-pattern (map)(opt)
  ;   :max-count (integer)(opt)
  ;   :search-pattern (map)(opt)
  ;   :skip (integer)(opt)
  ;   :sort-pattern (vectors in vector)(opt)}
  ;
  ; @usage
  ;  (mongo-db/get-pipeline {:field-pattern  {:namespace/name {:$concat [:$namespace/first-name " " :$namespace/last-name]}
  ;                          :filter-pattern {:$or [{:namespace/my-key   false}
  ;                                                 {:namespace/my-key   nil}]}
  ;                          :search-pattern {:$or [{:namespace/my-key   "Xyz"}
  ;                                                 {:namespace/your-key "Xyz"}]}
  ;                          :sort-pattern   {:namespace/my-key -1}
  ;                          :max-count 20
  ;                          :skip      40})
  ;
  ; @return (maps in vector)
  [{:keys [field-pattern filter-pattern max-count search-pattern skip sort-pattern]}]
  (cond-> [] field-pattern  (conj {"$addFields"      (add-fields-query field-pattern)})
             :match         (conj {"$match" {"$and" [(filter-query     filter-pattern) (search-query search-pattern)]}})
             sort-pattern   (conj {"$sort"           (sort-query       sort-pattern)})
             skip           (conj {"$skip"           (param            skip)})
             max-count      (conj {"$limit"          (param            max-count)})))

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
  [{"$match" {"$and" [(filter-query filter-pattern)
                      (search-query search-pattern)]}}])
