
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.update.events
    (:require [plugins.item-browser.backup.events :as backup.events]
              [plugins.item-browser.backup.subs   :as backup.subs]
              [plugins.item-browser.items.events  :as items.events]
              [plugins.item-browser.items.subs    :as items.subs]
              [plugins.item-browser.mount.subs    :as mount.subs]
              [x.app-core.api                     :refer [r]]
              [x.app-ui.api                       :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) changes
  ;
  ; @return (map)
  [db [_ browser-id item-id changes]]
  (let [items-path (r mount.subs/get-body-prop db browser-id :items-path)
        item-dex   (r items.subs/get-item-dex  db browser-id item-id)]
       (update-in db (conj items-path item-dex) merge changes)))

(defn revert-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (let [items-path  (r mount.subs/get-body-prop    db browser-id :items-path)
        backup-item (r backup.subs/get-backup-item db browser-id item-id)
        item-dex    (r items.subs/get-item-dex     db browser-id item-id)]
       (assoc-in db (conj items-path item-dex) backup-item)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (as-> db % (r backup.events/backup-item! % browser-id item-id)
             (r items.events/disable-item! % browser-id item-id)
             (r ui/fake-process!           % 15)))

(defn delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (r items.events/enable-item! db browser-id item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) changes
  ;
  ; @return (map)
  [db [_ browser-id item-id changes]]
  (as-> db % (r backup.events/backup-item! % browser-id item-id)
             (r items.events/disable-item! % browser-id item-id)
             (r apply-changes!             % browser-id item-id changes)))

(defn item-updated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (r items.events/enable-item! db browser-id item-id))

(defn update-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (as-> db % (r revert-changes!           % browser-id item-id)
             (r items.events/enable-item! % browser-id item-id)))
