
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.events
    (:require [plugins.item-browser.download.subs :as download.subs]
              [x.app-core.api                     :refer [r]]
              [x.app-db.api                       :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-downloaded-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (let [resolver-id (r download.subs/get-resolver-id db extension-id item-namespace :get)
        document    (get server-response resolver-id)]
       ; XXX#3907
       ; Az item-lister pluginnal megegyezően az item-browser plugin is névtér nélkül tárolja
       ; a letöltött dokumentumot
       (let [document    (db/document->non-namespaced-document document)
             document-id (get document :id)]
            (assoc-in db [extension-id :item-browser/data-items document-id] document))))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (r store-downloaded-item! db extension-id item-namespace server-response))
