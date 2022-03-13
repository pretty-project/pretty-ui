
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.update.subs
    (:require [plugins.item-lister.core.subs :as core.subs]
              [mid-fruits.keyword            :as keyword]
              [mid-fruits.vector             :as vector]
              [x.app-core.api                :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-mutation-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (r update.subs/get-mutation-name db :my-extension :my-type :delete)
  ;  =>
  ;  "my-handler/delete-items!"
  ;
  ; @return (string)
  [db [_ extension-id item-namespace action-key]]
  (let [handler-key (r core.subs/get-meta-item db extension-id item-namespace :handler-key)]
       (str (name handler-key) "/"
            (name action-key)  "-items!")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-deleted-item-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @example
  ;  (r subs/server-response->deleted-item-ids :my-extension :my-type {my-handler/delete-items! ["my-item"]})
  ;  =>
  ;  ["my-item"]
  ;
  ; @return (strings in vector)
  [db [_ extension-id item-namespace server-response]]
  (let [mutation-name (r get-mutation-name db extension-id item-namespace :delete)]
       (get server-response (symbol mutation-name))))

(defn get-duplicated-item-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @example
  ;  (r subs//server-response->duplicated-item-ids :my-extension :my-type {my-handler/duplicate-items! [{:my-type/id "my-item"}]})
  ;  =>
  ;  ["my-item"]
  ;
  ; @return (strings in vector)
  [db [_ extension-id item-namespace server-response]]
  (let [item-id-key   (keyword/add-namespace item-namespace :id)
        mutation-name (r get-mutation-name db extension-id item-namespace :duplicate)
        copy-items    (get server-response (symbol mutation-name))]
       (vector/->items copy-items item-id-key)))
