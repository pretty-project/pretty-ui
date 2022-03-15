
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.backup.events
    (:require [mid-fruits.map                 :as map]
              [mid-fruits.vector              :as vector]
              [plugins.item-lister.core.subs  :as core.subs]
              [plugins.item-lister.items.subs :as items.subs]
              [plugins.item-lister.mount.subs :as mount.subs]
              [x.app-core.api                 :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn backup-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [items-path     (r mount.subs/get-body-prop db extension-id item-namespace :items-path)
        selected-items (r core.subs/get-meta-item  db extension-id item-namespace :selected-items)]
       (letfn [(f [db item-dex]
                  (let [item-id (get-in db (vector/concat-items items-path [item-dex :id]))
                        item    (get-in db (vector/conj-item    items-path item-dex))]
                       (assoc-in db [:plugins :item-lister/backup-items extension-id item-id] item)))]
              (reduce f db selected-items))))

(defn clean-backup-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) item-ids
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-ids]]
  (update-in db [:plugins :item-lister/backup-items extension-id] map/remove-items item-ids))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/clean-backup-items! clean-backup-items!)
