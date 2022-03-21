
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.download.events
    (:require [plugins.item-browser.download.subs :as download.subs]
              [plugins.item-browser.mount.subs    :as mount.subs]
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
  ; XXX#3907
  ; Az item-lister pluginnal megegyezően az item-browser plugin is névtér nélkül tárolja a letöltött dokumentumot
  (let [resolver-id (r download.subs/get-resolver-id db extension-id item-namespace :get)
        item-path   (r mount.subs/get-body-prop      db extension-id item-namespace :item-path)
        document    (-> server-response resolver-id db/document->non-namespaced-document)]
       (assoc-in db item-path document)))

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
