
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.engine
    (:require [mid-fruits.keyword             :as keyword]
              [mid-fruits.vector              :as vector]
              [mid-plugins.item-lister.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-lister.engine
(def request-id               engine/request-id)
(def mutation-name            engine/mutation-name)
(def resolver-id              engine/resolver-id)
(def collection-name          engine/collection-name)
(def new-item-uri             engine/new-item-uri)
(def add-new-item-event       engine/add-new-item-event)
(def transfer-id              engine/transfer-id)
(def route-id                 engine/route-id)
(def route-template           engine/route-template)
(def component-id             engine/component-id)
(def dialog-id                engine/dialog-id)
(def load-extension-event     engine/load-extension-event)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn server-response->deleted-item-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @example
  ;  (engine/server-response->deleted-item-ids :my-extension :my-type
  ;    {my-extension.my-type-lister/delete-items! ["my-item"]})
  ;  =>
  ;  ["my-item"]
  ;
  ; @return (strings in vector)
  [extension-id item-namespace server-response]
  (let [mutation-name (mutation-name extension-id item-namespace :delete)]
       (get server-response (symbol mutation-name))))

(defn server-response->duplicated-item-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @example
  ;  (engine/server-response->duplicated-item-ids :my-extension :my-type
  ;    {my-extension.my-type-lister/duplicate-items! [{:my-type/id "my-item"}]})
  ;  =>
  ;  ["my-item"]
  ;
  ; @return (strings in vector)
  [extension-id item-namespace server-response]
  (let [item-id-key   (keyword/add-namespace      item-namespace :id)
        mutation-name (mutation-name extension-id item-namespace :duplicate)
        copy-items    (get server-response (symbol mutation-name))]
       (vector/->items copy-items item-id-key)))

(defn order-by-label-f
  ; @param (namespaced keyword) order-by
  ;
  ; @example
  ;  (engine/order-by-label-f :name/ascending)
  ;  =>
  ;  :by-name-ascending
  ;
  ; @return (keyword)
  [order-by]
  (keyword (str "by-" (namespace order-by)
                "-"   (name      order-by))))
