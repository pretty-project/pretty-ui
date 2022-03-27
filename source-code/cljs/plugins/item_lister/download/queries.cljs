
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.download.queries
    (:require [plugins.item-lister.core.subs     :as core.subs]
              [plugins.item-lister.download.subs :as download.subs]
              [plugins.item-lister.mount.subs    :as mount.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-items-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  ;  {:downloaded-item-count (integer)
  ;   :download-limit (integer)
  ;   :filter-pattern (maps in vector)
  ;   :order-by (namespaced keyword)
  ;   :reload-items? (boolean)
  ;   :search-keys (keywords in vector)
  ;   :search-term (string)}
  [db [_ lister-id]]
  (merge (r core.subs/get-meta-item db lister-id :default-query-params)
         {:download-limit        (r mount.subs/get-body-prop            db lister-id :download-limit)
          :order-by              (r core.subs/get-meta-item             db lister-id :order-by)
          :reload-items?         (r core.subs/get-meta-item             db lister-id :reload-mode?)
          :search-keys           (r mount.subs/get-body-prop            db lister-id :search-keys)
          :search-term           (r core.subs/get-meta-item             db lister-id :search-term)
          :downloaded-item-count (r core.subs/get-downloaded-item-count db lister-id)
          :filter-pattern        (r core.subs/get-filter-pattern        db lister-id)}))

(defn get-request-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (vector)
  [db [_ lister-id]]
  (let [resolver-id    (r download.subs/get-resolver-id    db lister-id :get-items)
        resolver-props (r get-request-items-resolver-props db lister-id)]
       [`(~resolver-id ~resolver-props)]))
