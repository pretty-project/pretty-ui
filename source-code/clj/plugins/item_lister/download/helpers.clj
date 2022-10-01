
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.download.helpers
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.math    :as math]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]
              [mongo-db.api       :as mongo-db]
              [pathom.api         :as pathom]
              [re-frame.api       :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sort-pattern
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (namespaced keyword) order-by
  ;
  ; @example
  ;  (download.helpers/sort-pattern :my-lister :name/ascending)
  ;  =>
  ;  {:my-type/name 1}
  ;
  ; @return (map)
  [lister-id order-by]
  (let [item-namespace @(r/subscribe [:item-lister/get-lister-prop lister-id :item-namespace])
        order-key       (namespace order-by)
        direction       (name      order-by)]
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
  ;  (download.helpers/env->max-count {... .../params {:downloaded-item-count 24
  ;                                                    :download-limit        20
  ;                                                    :reload-items?         false}})
  ;  =>
  ;  20
  ;
  ; @example
  ;  (download.helpers/env->max-count {... .../params {:downloaded-item-count 24
  ;                                                    :download-limit        20
  ;                                                    :reload-items?         true}})
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
  ; @param (keyword) lister-id
  ;
  ; @example
  ;  (item-lister/env->sort-pattern {...} :my-lister)
  ;  =>
  ;  {:my-type/name 1}
  ;
  ; @return (map)
  [env lister-id]
  (let [order-by (pathom/env->param env :order-by)]
       (sort-pattern lister-id order-by)))

(defn env->search-pattern
  ; @param (map) env
  ; @param (keyword) lister-id
  ;
  ; @example
  ;  (item-lister/env->search-pattern {...} :my-lister)
  ;  =>
  ;  {:$or [{:my-type/name "..."} {...}]}
  ;
  ; @return (map)
  ;  {:$or (maps in vector)}
  [env lister-id]
  (let [item-namespace @(r/subscribe [:item-lister/get-lister-prop lister-id :item-namespace])
        search-keys     (pathom/env->param env :search-keys)
        search-term     (pathom/env->param env :search-term)]
       (if (string/nonempty? search-term)
           {:$or (vector/->items search-keys #(return {(keyword/add-namespace item-namespace %) search-term}))})))

(defn env->pipeline-props
  ; @param (map) env
  ; @param (keyword) lister-id
  ;
  ; @example
  ;  (item-lister/env->pipeline-props {...} :my-lister)
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
  [env lister-id]
  {:max-count      (env->max-count      env)
   :skip           (env->skip           env)
   :field-pattern  (pathom/env->param   env :field-pattern)
   :filter-pattern (pathom/env->param   env :filter-pattern)
   :unset-pattern  (pathom/env->param   env :unset-pattern)
   :search-pattern (env->search-pattern env lister-id)
   :sort-pattern   (env->sort-pattern   env lister-id)})

(defn env->get-pipeline
  ; @param (map) env
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (item-lister/env->get-pipeline {...} :my-lister)
  ;
  ; @return (maps in vector)
  [env lister-id]
  (let [pipeline-props (env->pipeline-props env lister-id)]
       (mongo-db/get-pipeline pipeline-props)))

(defn env->count-pipeline
  ; @param (map) env
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (item-lister/env->count-pipeline {...} :my-lister)
  ;
  ; @return (maps in vector)
  [env lister-id]
  (let [pipeline-props (env->pipeline-props env lister-id)]
       (mongo-db/count-pipeline pipeline-props)))
