
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.events
    (:require [mid-fruits.candy                  :refer [return]]
              [plugins.item-editor.backup.events :as backup.events]
              [plugins.item-editor.body.subs     :as body.subs]
              [plugins.item-editor.core.events   :as core.events]
              [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.download.subs :as download.subs]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-db.api                      :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items editor-id :data-received?] true))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; Ha az [:item-editor/request-item! ...] esemény megtörténésekor az item-editor plugin
  ; már használatban van, akkor az adatok letöltése előtt szükséges visszaléptetni a plugint
  ; {:data-received? false} állapotba, hogy a letöltés idejére újra megjelenjen a letöltésjelző.
  ; Pl.: Ha a felhasználó egy elem szerkesztése közben duplikálja az elemet, majd a megjelenő
  ;      értesítésen a "Másolat szerkesztése" gombra kattint, akkor az item-editor plugin
  ;      letölti a másolat elemet, miközben szükséges megjeleníteni a letöltésjelzőt!
  (r core.events/reset-downloads! db editor-id))

(defn store-downloaded-suggestions!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ editor-id server-response]]
  ; XXX#3907
  (let [suggestions-path (r body.subs/get-body-prop db editor-id :suggestions-path)
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
  ; - XXX#3907
  ;   Az item-lister pluginnal megegyezően az item-editor plugin is névtér nélkül tárolja a letöltött dokumentumot
  ;
  ; - A letöltött dokumentum a merge függvény használatával kerül eltárolásra, így az esetlegesen
  ;   a body komponens számára {:initial-item {...}} tulajdonságként átadott értékek nem íródnak felül.
  (let [resolver-id (r download.subs/get-resolver-id db editor-id :get-item)
        item-path   (r body.subs/get-body-prop       db editor-id :item-path)
        document    (-> server-response resolver-id db/document->non-namespaced-document)]
       (as-> db % (update-in % item-path merge document)
                  (r backup.events/backup-current-item! % editor-id))))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ editor-id server-response]]
  (cond-> ; Set {:data-received? true} state ...
          (r data-received db editor-id)
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

(defn load-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (if (r core.subs/get-meta-item db editor-id :recovery-mode?)
      (r backup.events/recover-item! db editor-id)
      (return db)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-editor/data-received data-received)
