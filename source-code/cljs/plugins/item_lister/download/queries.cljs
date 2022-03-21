
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
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:downloaded-item-count (integer)
  ;   :download-limit (integer)
  ;   :filter-pattern (maps in vector)
  ;   :order-by (namespaced keyword)
  ;   :reload-items? (boolean)
  ;   :search-keys (keywords in vector)
  ;   :search-term (string)}
  [db [_ extension-id item-namespace]]
  {:download-limit        (r mount.subs/get-body-prop            db extension-id item-namespace :download-limit)
   :order-by              (r core.subs/get-meta-item             db extension-id item-namespace :order-by)
   :reload-items?         (r core.subs/get-meta-item             db extension-id item-namespace :reload-mode?)
   :search-keys           (r mount.subs/get-body-prop            db extension-id item-namespace :search-keys)
   :search-term           (r core.subs/get-meta-item             db extension-id item-namespace :search-term)
   :downloaded-item-count (r core.subs/get-downloaded-item-count db extension-id item-namespace)
   :filter-pattern        (r core.subs/get-filter-pattern        db extension-id item-namespace)

   ; TEMP
   ; Az {:item-id ...} értéke az item-browser plugin számára szükséges!
   :item-id (get-in db [extension-id :item-lister/meta-items :item-id])})

(defn get-request-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (let [resolver-id    (r download.subs/get-resolver-id    db extension-id item-namespace :get)
        resolver-props (r get-request-items-resolver-props db extension-id item-namespace)]
       [:debug `(~resolver-id ~resolver-props)]))
