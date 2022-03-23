
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.queries
    (:require [plugins.item-browser.core.subs     :as core.subs]
              [plugins.item-browser.download.subs :as download.subs]
              [x.app-core.api                     :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-item-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:item-id (string)}
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r core.subs/get-current-item-id db extension-id item-namespace)]
       (merge (r core.subs/get-meta-item db extension-id item-namespace :default-query-params)
              {:item-id current-item-id})))

(defn get-request-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (let [resolver-id    (r download.subs/get-resolver-id   db extension-id item-namespace :get)
        resolver-props (r get-request-item-resolver-props db extension-id item-namespace)]
       [`(~resolver-id ~resolver-props)]))
