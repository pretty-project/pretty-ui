
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.events
    (:require [plugins.item-editor.backup.events :as backup.events]
              [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.download.subs :as download.subs]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-db.api                      :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-downloaded-suggestions!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ extension-id item-namespace server-response]]
  (let [suggestions (get server-response :item-editor/get-item-suggestions)]
       (assoc-in db [extension-id :item-editor/meta-items :suggestions] suggestions)))

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
       ; Az item-lister pluginnal megegyezően az item-editor plugin is névtér nélkül tárolja
       ; a letöltött dokumentumot
       (let [document (db/document->non-namespaced-document document)]
            (as-> db % (assoc-in % [extension-id :item-editor/data-items] document)
                       (r backup.events/backup-current-item! % extension-id item-namespace)))))

(defn data-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (assoc-in db [extension-id :item-editor/meta-items :data-received?] true))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [event-id extension-id item-namespace server-response]]
  (cond-> db (r download.subs/download-item?        db extension-id item-namespace)
             (store-downloaded-item!         [event-id extension-id item-namespace server-response])
             (r download.subs/download-suggestions? db extension-id item-namespace)
             (store-downloaded-suggestions!  [event-id extension-id item-namespace server-response])
             (r core.subs/get-meta-item             db extension-id item-namespace :recovery-mode?)
             (recover-item!                  [event-id extension-id item-namespace])
             :data-received   (data-received [event-id extension-id item-namespace])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-editor/receive-item! receive-item!)
