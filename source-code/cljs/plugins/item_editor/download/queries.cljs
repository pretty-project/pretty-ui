
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.queries
    (:require [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.download.subs :as download.subs]
              [plugins.item-editor.mount.subs    :as mount.subs]
              [x.app-core.api                    :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  [:debug (if (r core.subs/download-item? db extension-id item-namespace)
              ; If download item ...
              (let [resolver-id     (r download.subs/get-resolver-id db extension-id item-namespace :get)
                    current-item-id (r core.subs/get-current-item-id db extension-id item-namespace)]
                  `(~resolver-id ~{:item-id current-item-id})))
          (if (r core.subs/download-suggestions? db extension-id item-namespace)
              ; If download suggestions ...
              (let [suggestion-keys (r mount.subs/get-body-prop db extension-id item-namespace :suggestion-keys)]
                  `(:item-editor/get-item-suggestions {:suggestion-keys ~suggestion-keys
                                                       :extension-id    ~extension-id
                                                       :item-namespace  ~item-namespace})))])
