
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.queries
    (:require [app-plugins.item-lister.engine :as engine]
              [app-plugins.item-lister.subs   :as subs]
              [mid-fruits.candy               :refer [param return]]
              [x.app-core.api                 :as a :refer [r]]))



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
  {:download-limit        (r subs/get-meta-item             db extension-id item-namespace :download-limit)
   :order-by              (r subs/get-meta-item             db extension-id item-namespace :order-by)
   :reload-items?         (r subs/get-meta-item             db extension-id item-namespace :reload-mode?)
   :search-keys           (r subs/get-meta-item             db extension-id item-namespace :search-keys)
   :downloaded-item-count (r subs/get-downloaded-item-count db extension-id item-namespace)
   :filter-pattern        (r subs/get-filter-pattern        db extension-id item-namespace)
   :search-term           (r subs/get-search-term           db extension-id item-namespace)

   ; TEMP
   ; Az {:item-id ...} értéke az item-browser plugin számára szükséges!
   :item-id (get-in db [extension-id :item-browser/meta-items :item-id])})

(defn get-request-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (let [resolver-id    (r subs/get-resolver-id             db extension-id item-namespace :get)
        resolver-props (r get-request-items-resolver-props db extension-id item-namespace)]
       [:debug `(~resolver-id ~resolver-props)]))

(defn get-delete-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-ids]]
  (let [mutation-name (r subs/get-mutation-name db extension-id item-namespace :delete)]
       [:debug `(~(symbol mutation-name) ~{:item-ids item-ids})]))

(defn get-undo-delete-items-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (map)
  ;  {:items (namespaced maps in vector)}
  [db [_ extension-id item-namespace item-ids]]
  (let [exported-items (r subs/export-backup-items db extension-id item-namespace item-ids)]
       {:items exported-items}))

(defn get-undo-delete-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-ids]]
  (let [mutation-name  (r subs/get-mutation-name               db extension-id item-namespace :undo-delete)
        mutation-props (r get-undo-delete-items-mutation-props db extension-id item-namespace item-ids)]
       [:debug `(~(symbol mutation-name) ~mutation-props)]))

(defn get-duplicate-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-ids]]
  (let [mutation-name (r subs/get-mutation-name db extension-id item-namespace :duplicate)]
       [:debug `(~(symbol mutation-name) ~{:item-ids item-ids})]))

(defn get-undo-duplicate-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) copy-ids
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace copy-ids]]
  (let [mutation-name (r subs/get-mutation-name db extension-id item-namespace :undo-duplicate)]
       [:debug `(~(symbol mutation-name) ~{:item-ids copy-ids})]))
