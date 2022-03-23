
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.queries
    (:require [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.download.subs :as download.subs]
              [plugins.item-editor.mount.subs    :as mount.subs]
              [x.app-core.api                    :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-suggestions-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:item (namespaced map)}
  [db [_ extension-id item-namespace]]
  (let [suggestion-keys (r mount.subs/get-body-prop db extension-id item-namespace :suggestion-keys)]
       (merge (r core.subs/get-meta-item db extension-id item-namespace :default-resolver-params)
              {:suggestion-keys suggestion-keys
               :extension-id    extension-id
               :item-namespace  item-namespace})))

(defn get-request-item-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:item (namespaced map)}
  [db [_ extension-id item-namespace]]
  (let [current-item-id (r core.subs/get-current-item-id db extension-id item-namespace)]
       (merge (r core.subs/get-meta-item db extension-id item-namespace :default-resolver-params)
              {:item-id current-item-id})))

(defn get-request-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  [(if (r core.subs/download-item? db extension-id item-namespace)
       ; If download item ...
       (let [resolver-id    (r download.subs/get-resolver-id   db extension-id item-namespace :get)
             resolver-props (r get-request-item-resolver-props db extension-id item-namespace)]
           `(~resolver-id ~resolver-props)))
   (if (r core.subs/download-suggestions? db extension-id item-namespace)
       ; If download suggestions ...
       (let [resolver-id :item-editor/get-item-suggestions
             resolver-props (r get-request-suggestions-resolver-props db extension-id item-namespace)]
           `(~resolver-id ~resolver-props)))])
