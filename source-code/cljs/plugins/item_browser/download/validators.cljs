
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.validators
    (:require [plugins.item-browser.download.subs :as download.subs]
              [x.app-core.api                     :refer [r]]
              [x.app-db.api                       :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-item-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace server-response]]
  (let [resolver-id (r download.subs/get-resolver-id db extension-id item-namespace :get)
        document    (get server-response resolver-id)]
       (db/document->document-namespaced? document)))