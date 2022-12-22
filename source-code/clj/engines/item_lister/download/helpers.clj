
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.download.helpers
    (:require [candy.api    :refer [return]]
              [keyword.api  :as keyword]
              [math.api     :as math]
              [string.api   :as string]
              [mongo-db.api :as mongo-db]
              [pathom.api   :as pathom]
              [re-frame.api :as r]
              [vector.api   :as vector]
              [x.user.api   :as x.user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sort-pattern
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (namespaced keyword) order-by
  ;
  ; @example
  ; (sort-pattern :my-lister :name/ascending)
  ; =>
  ; {:my-type/name 1}
  ;
  ; @return (map)
  [lister-id order-by]
  ; https://www.mongodb.com/docs/manual/reference/method/cursor.sort/#sort-cursor-stable-sorting
  ; To avoid getting repeated results, a key has to be added which points to unique values!
  ;
  ; The sort-pattern function adds the :id key to the sort-pattern, which solves the problem,
  ; but if any other key added which points to unique values that's unnecessary.
  (let [item-namespace @(r/subscribe [:item-lister/get-lister-prop lister-id :item-namespace])
        order-key       (namespace order-by)
        direction       (name      order-by)]
       {(keyword/add-namespace item-namespace order-key)
        (case direction "ascending" 1 "descending" -1)
        (keyword/add-namespace item-namespace :id)  1}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- env->max-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;
  ; @example
  ; (env->max-count {... .../params {:download-limit    20
  ;                                  :listed-item-count 24
  ;                                  :reload-items?     false}})
  ; =>
  ; 20
  ;
  ; @example
  ; (env->max-count {... .../params {:download-limit    20
  ;                                  :listed-item-count 24
  ;                                  :reload-items?     true}})
  ; =>
  ; 40
  ;
  ; @return (integer)
  [env]
  (let [download-limit (pathom/env->param env :download-limit)]
       (if-let [reload-items? (pathom/env->param env :reload-items?)]
               ; Ha az item-lister {:reload-items? true} állapotban van, akkor a mongo-db aggregation pipeline
               ; számára átadott max-count tulajdonság értéke a download-limit értékének legkisebb olyan többszöröse,
               ; ami nagyobb, mint a listed-item-count értéke ...
               (let [listed-item-count (pathom/env->param env :listed-item-count)
                     ; Ha a listed-item-count értéke 0, akkor matematikai szempontból nem tartozik
                     ; az első tartományba (pl. 1-20 elem) és a math/domain-ceil függvény visszatérési
                     ; értéke 0 lenne!
                     listed-item-count (max listed-item-count 1)]
                    (math/domain-ceil listed-item-count download-limit))
               ; Ha az item-lister NINCS {:reload-items? true} állapotban, akkor a mongo-db aggregation pipeline
               ; számára átadott max-count tulajdonság értéke megegyezik az item-lister engine download-limit értékével ...
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
          (pathom/env->param env :listed-item-count)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->sort-pattern
  ; @param (map) env
  ; @param (keyword) lister-id
  ;
  ; @example
  ; (env->sort-pattern {...} :my-lister)
  ; =>
  ; {:my-type/name 1}
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
  ; (env->search-pattern {...} :my-lister)
  ; =>
  ; {:$or [{:my-type/name "..."} {...}]}
  ;
  ; @return (map)
  ; {:$or (maps in vector)}
  [env lister-id]
  (let [item-namespace @(r/subscribe [:item-lister/get-lister-prop lister-id :item-namespace])
        search-keys     (pathom/env->param env :search-keys)
        search-term     (pathom/env->param env :search-term)]
       (if (string/nonblank? search-term)
           {:$or (vector/->items search-keys #(return {(keyword/add-namespace item-namespace %) search-term}))})))

(defn env->pipeline-options
  ; @param (map) env
  ; @param (keyword) lister-id
  ;
  ; @example
  ; (env->pipeline-options {...} :my-lister)
  ; =>
  ; {:locale "en"}
  ;
  ; @return (map)
  ; {:locale (string)}
  [{:keys [request]} lister-id]
  (let [selected-language (x.user/request->user-settings-item request :selected-language)]
       {:locale (name selected-language)}))

(defn env->pipeline-props
  ; @param (map) env
  ; @param (keyword) lister-id
  ;
  ; @example
  ; (env->pipeline-props {...} :my-lister)
  ; =>
  ; {:max-count 20
  ;  :skip       0
  ;  :filter-pattern {:$or [{:my-type/my-key "..."} {...}]}
  ;  :search-pattern {:$or [{:my-type/name   "..."} {...}]}
  ;  :sort-pattern   {:my-type/name 1}
  ;  :unset-pattern  [:my-type/my-key]}
  ;
  ; @return (map)
  ; {:field-pattern (map)
  ;  :filter-pattern (map)
  ;  :max-count (integer)
  ;  :search-pattern (map)
  ;  :skip (integer)
  ;  :sort-pattern (map)
  ;  :unset-pattern (namespaced keywords in vector)}
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
  ; (env->get-pipeline {...} :my-lister)
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
  ; (env->count-pipeline {...} :my-lister)
  ;
  ; @return (maps in vector)
  [env lister-id]
  (let [pipeline-props (env->pipeline-props env lister-id)]
       (mongo-db/count-pipeline pipeline-props)))
