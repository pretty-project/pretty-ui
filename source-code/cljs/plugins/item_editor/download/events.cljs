
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.events
    (:require [mid-fruits.map                    :refer [dissoc-in]]
              [plugins.item-editor.backup.events :as backup.events]
              [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.download.subs :as download.subs]
              [plugins.item-editor.mount.subs    :as mount.subs]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-db.api                      :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [item-path        (r mount.subs/get-body-prop db extension-id item-namespace :item-path)
        suggestions-path (r mount.subs/get-body-prop db extension-id item-namespace :suggestions-path)]
       (-> db (dissoc-in item-path)
              (dissoc-in suggestions-path))))



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
  ; XXX#3907
  (let [suggestions-path (r mount.subs/get-body-prop db extension-id item-namespace :suggestions-path)
        suggestions      (-> server-response :item-editor/get-item-suggestions db/document->non-namespaced-document)]
       (assoc-in db suggestions-path suggestions)))

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
  ; Az item-lister pluginnal megegyezően az item-editor plugin is névtér nélkül tárolja a letöltött dokumentumot
  (let [resolver-id (r download.subs/get-resolver-id db extension-id item-namespace :get)
        item-path   (r mount.subs/get-body-prop      db extension-id item-namespace :item-path)
        document    (-> server-response resolver-id db/document->non-namespaced-document)]
       (as-> db % (assoc-in % item-path document)
                  (r backup.events/backup-current-item! % extension-id item-namespace))))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [event-id extension-id item-namespace server-response]]
  (cond-> ; Set {:data-received? true} state ...
          (assoc-in db [:plugins :item-editor/meta-items extension-id :data-received?] true)
          ; If editor downloading item ...
          (r core.subs/download-item? db extension-id item-namespace)
          (as-> % (r store-downloaded-item! % extension-id item-namespace server-response))
          ; If editor downloading suggestions ...
          (r core.subs/download-suggestions? db extension-id item-namespace)
          (as-> % (r store-downloaded-suggestions! % extension-id item-namespace server-response))
          ; If editor in recovery-mode ...
          (r core.subs/get-meta-item db extension-id item-namespace :recovery-mode?)
          (as-> % (r backup.events/recover-item! % extension-id item-namespace))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-editor/receive-item! receive-item!)
