
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.events
    (:require [plugins.item-editor.backup.events :as backup.events]
              [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.download.subs :as download.subs]
              [plugins.item-editor.mount.subs    :as mount.subs]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-db.api                      :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-downloaded-suggestions!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ editor-id server-response]]
  ; XXX#3907
  (let [suggestions-path (r mount.subs/get-body-prop db editor-id :suggestions-path)
        suggestions      (-> server-response :item-editor/get-item-suggestions db/document->non-namespaced-document)]
       (assoc-in db suggestions-path suggestions)))

(defn store-downloaded-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ editor-id server-response]]
  ; XXX#3907
  ; Az item-lister pluginnal megegyezően az item-editor plugin is névtér nélkül tárolja a letöltött dokumentumot
  (let [resolver-id (r download.subs/get-resolver-id db editor-id :get-item)
        item-path   (r mount.subs/get-body-prop      db editor-id :item-path)
        document    (-> server-response resolver-id db/document->non-namespaced-document)]
       (as-> db % (assoc-in % item-path document)
                  (r backup.events/backup-current-item! % editor-id))))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [event-id editor-id server-response]]
  (cond-> ; Set {:data-received? true} state ...
          (assoc-in db [:plugins :plugin-handler/meta-items editor-id :data-received?] true)
          ; If editor downloading item ...
          (r core.subs/download-item? db editor-id)
          (as-> % (r store-downloaded-item! % editor-id server-response))
          ; If editor downloading suggestions ...
          (r core.subs/download-suggestions? db editor-id)
          (as-> % (r store-downloaded-suggestions! % editor-id server-response))
          ; If editor in recovery-mode ...
          (r core.subs/get-meta-item db editor-id :recovery-mode?)
          (as-> % (r backup.events/recover-item! % editor-id))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-editor/receive-item! receive-item!)
