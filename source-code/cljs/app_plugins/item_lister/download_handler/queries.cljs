
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.download-handler.queries
    (:require [app-plugins.item-lister.download-handler.subs :as download-handler.subs]
              [app-plugins.item-lister.lister-handler.subs   :as lister-handler.subs]
              [x.app-core.api                                :as a :refer [r]]))



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
  {:download-limit        (r lister-handler.subs/get-meta-item               db extension-id item-namespace :download-limit)
   :order-by              (r lister-handler.subs/get-meta-item               db extension-id item-namespace :order-by)
   :reload-items?         (r lister-handler.subs/get-meta-item               db extension-id item-namespace :reload-mode?)
   :search-keys           (r lister-handler.subs/get-meta-item               db extension-id item-namespace :search-keys)
   :filter-pattern        (r lister-handler.subs/get-filter-pattern          db extension-id item-namespace)
   :search-term           (r lister-handler.subs/get-search-term             db extension-id item-namespace)
   :downloaded-item-count (r download-handler.subs/get-downloaded-item-count db extension-id item-namespace)

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
  (let [resolver-id    (r download-handler.subs/get-resolver-id db extension-id item-namespace :get)
        resolver-props (r get-request-items-resolver-props      db extension-id item-namespace)]
       [:debug `(~resolver-id ~resolver-props)]))
