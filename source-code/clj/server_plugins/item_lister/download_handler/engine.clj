
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.download-handler.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.math    :as math]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]
              [mongo-db.api       :as mongo-db]
              [pathom.api         :as pathom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sort-pattern
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (namespaced keyword) order-by
  ;
  ; @example
  ;  (sort-pattern :my-extension :my-type :name/ascending)
  ;  =>
  ;  {:my-type/name 1}
  ;
  ; @return (map)
  [_ item-namespace order-by]
  (let [order-key (namespace order-by)
        direction (name      order-by)]
       {(keyword/add-namespace item-namespace order-key)
        (case direction "ascending" 1 "descending" -1)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- env->max-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;
  ; @example
  ;  (env->max-count {... .../params {:downloaded-item-count 24
  ;                                   :download-limit        20
  ;                                   :reload-items?         false}})
  ;  =>
  ;  20
  ;
  ; @example
  ;  (env->max-count {... .../params {:downloaded-item-count 24
  ;                                   :download-limit        20
  ;                                   :reload-items?         true}})
  ;  =>
  ;  40
  ;
  ; @return (integer)
  [env]
  (let [download-limit (pathom/env->param env :download-limit)]
       (if-let [reload-items? (pathom/env->param env :reload-items?)]
               ; Ha az item-lister {:reload-items? true} állapotban van, akkor a mongo-db aggregation pipeline
               ; számára átadott max-count tulajdonság értéke a download-limit értékének legkisebb olyan többszöröse,
               ; ami nagyobb, mint a downloaded-item-count értéke ...
               (let [downloaded-item-count (pathom/env->param env :downloaded-item-count)
                     ; Ha a downloaded-item-count értéke 0, akkor matematikai szempontból nem tartozik
                     ; az első tartományba (pl. 1-20 elem) és a math/domain-ceil függvény visszatérési
                     ; értéke 0 lenne!
                     downloaded-item-count (max downloaded-item-count 1)]
                    (math/domain-ceil downloaded-item-count download-limit))
               ; Ha az item-lister NINCS {:reload-items? true} állapotban, akkor a mongo-db aggregation pipeline
               ; számára átadott max-count tulajdonság értéke megegyezik az item-lister plugin download-limit értékével ...
               (return download-limit))))

(defn- env->skip
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;
  ; @return (integer)
  [env]
  (if-let [reload-items? (pathom/env->param env :reload-items?)]
          (return 0)
          (pathom/env->param env :downloaded-item-count)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->sort-pattern
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (env->sort-pattern {...} :my-extension :my-type)
  ;  =>
  ;  {:my-type/name 1}
  ;
  ; @return (map)
  [env extension-id item-namespace]
  (let [order-by (pathom/env->param env :order-by)]
       (sort-pattern extension-id item-namespace order-by)))

(defn env->search-pattern
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (env->search-pattern {...} :my-extension :my-type)
  ;  =>
  ;  {:$or [{:my-type/name "..."} {...}]}
  ;
  ; @return (map)
  ;  {:$or (maps in vector)}
  [env _ item-namespace]
  (let [search-keys (pathom/env->param env :search-keys)
        search-term (pathom/env->param env :search-term)]
       (if (string/nonempty? search-term)
           {:$or (vector/->items search-keys #(return {(keyword/add-namespace item-namespace %) search-term}))})))

(defn env->pipeline-props
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (env->pipeline-props {...} :my-extension :my-type)
  ;  =>
  ;  {:max-count 20
  ;   :skip       0
  ;   :filter-pattern {:$or [{:my-type/my-key "..."} {...}]}
  ;   :search-pattern {:$or [{:my-type/name   "..."} {...}]}
  ;   :sort-pattern   {:my-type/name 1}
  ;   :unset-pattern  [:my-type/my-key]}
  ;
  ; @return (map)
  ;  {:field-pattern (map)
  ;   :filter-pattern (map)
  ;   :max-count (integer)
  ;   :search-pattern (map)
  ;   :skip (integer)
  ;   :sort-pattern (map)
  ;   :unset-pattern (namespaced keywords in vector)}
  [env extension-id item-namespace]
  {:max-count      (env->max-count      env)
   :skip           (env->skip           env)
   :field-pattern  (pathom/env->param   env :field-pattern)
   :filter-pattern (pathom/env->param   env :filter-pattern)
   :unset-pattern  (pathom/env->param   env :unset-pattern)
   :search-pattern (env->search-pattern env extension-id item-namespace)
   :sort-pattern   (env->sort-pattern   env extension-id item-namespace)})

(defn env->get-pipeline
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (env->get-pipeline {...} :my-extension :my-type)
  ;
  ; @return (maps in vector)
  [env extension-id item-namespace]
  (let [pipeline-props (env->pipeline-props env extension-id item-namespace)]
       (mongo-db/get-pipeline pipeline-props)))

(defn env->count-pipeline
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (env->count-pipeline {...} :my-extension :my-type)
  ;
  ; @return (maps in vector)
  [env extension-id item-namespace]
  (let [pipeline-props (env->pipeline-props env extension-id item-namespace)]
       (mongo-db/count-pipeline pipeline-props)))
