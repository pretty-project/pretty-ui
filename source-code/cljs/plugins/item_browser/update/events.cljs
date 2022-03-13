
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.update.events
    (:require [plugins.item-browser.backup.subs  :as backup.subs]
              [plugins.item-browser.items.events :as items.events]
              [plugins.item-browser.items.subs   :as items.subs]
              [x.app-core.api                    :refer [r]]
              [x.app-ui.api                      :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ; @param (map) changes
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id changes]]
  (let [item-dex (r items.subs/get-item-dex db extension-id item-namespace item-id)]
       (update-in db [extension-id :item-lister/data-items item-dex] merge changes)))

(defn revert-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (let [backup-item (r backup.subs/get-backup-item db extension-id item-namespace item-id)
        item-dex    (r items.subs/get-item-dex     db extension-id item-namespace item-id)]
       (assoc-in db [extension-id :item-lister/data-items item-dex] backup-item)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (as-> db % (r backup-item!               % extension-id item-namespace item-id)
             (r items.events/disable-item! % extension-id item-namespace item-id)
             (r ui/fake-process!           % 15)))

(defn delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (r items.events/enable-item! db extension-id item-namespace item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ; @param (map) changes
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id changes]]
  (as-> db % (r backup.events/backup-item! % extension-id item-namespace item-id)
             (r items.events/disable-item! % extension-id item-namespace item-id)
             (r apply-changes!             % extension-id item-namespace item-id changes)))

(defn item-updated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (r items.events/enable-item! db extension-id item-namespace item-id))

(defn update-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (as-> db % (r revert-changes!           % extension-id item-namespace item-id)
             (r items.events/enable-item! % extension-id item-namespace item-id)))
