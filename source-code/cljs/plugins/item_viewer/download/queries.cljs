
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.download.queries
    (:require [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.download.subs :as download.subs]
              [x.app-core.api                    :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-item-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (map)
  ;  {:item-id (string)}
  [db [_ viewer-id]]
  (let [current-item-id (r core.subs/get-current-item-id db viewer-id)]
       (merge (r core.subs/get-meta-item db viewer-id :default-query-params)
              {:item-id current-item-id})))

(defn get-request-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (vector)
  [db [_ viewer-id]]
  (let [resolver-id    (r download.subs/get-resolver-id   db viewer-id :get-item)
        resolver-props (r get-request-item-resolver-props db viewer-id)]
      [`(~resolver-id ~resolver-props)]))
