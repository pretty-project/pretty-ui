
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
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  ;  {:item-id (string)}
  [db [_ browser-id]]
  (let [current-item-id (r core.subs/get-current-item-id db browser-id)]
       (merge (r core.subs/get-query-params db browser-id)
              {:item-id current-item-id})))

(defn get-request-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (vector)
  [db [_ browser-id]]
  (let [resolver-id    (r download.subs/get-resolver-id   db browser-id :get-item)
        resolver-props (r get-request-item-resolver-props db browser-id)]
       [`(~resolver-id ~resolver-props)]))
