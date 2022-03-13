
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.queries
    (:require [plugins.item-browser.core.subs     :as core.subs]
              [plugins.item-browser.download.subs :as download.subs]
              [x.app-core.api                     :refer [r]]))



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
  (let [resolver-id     (r download.subs/get-resolver-id db extension-id item-namespace :get)
        current-item-id (r core.subs/get-current-item-id db extension-id item-namespace)]
       [:debug `(~resolver-id ~{:item-id current-item-id})]))
