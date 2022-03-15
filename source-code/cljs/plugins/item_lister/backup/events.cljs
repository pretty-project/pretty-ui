
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.backup.events
    (:require [mid-fruits.map                 :as map]
              [plugins.item-lister.core.subs  :as core.subs]
              [plugins.item-lister.items.subs :as items.subs]
              [x.app-core.api                 :refer [r]]))



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
  (let [selected-items (r core.subs/get-meta-item db extension-id item-namespace :selected-items)]
       (letfn [(f [db item-dex]
                  (let [item-id (get-in db [extension-id :item-lister/data-items item-dex :id])
                        item    (get-in db [extension-id :item-lister/data-items item-dex])]
                       (assoc-in db [extension-id :item-lister/backup-items item-id] item)))]
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
  (update-in db [extension-id :item-lister/backup-items] map/remove-items item-ids))
