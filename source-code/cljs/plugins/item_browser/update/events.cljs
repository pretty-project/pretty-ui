
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.update.events
    (:require [plugins.item-browser.backup.events :as backup.events]
              [plugins.item-browser.backup.subs   :as backup.subs]
              [plugins.item-browser.body.subs     :as body.subs]
              [plugins.item-browser.items.events  :as items.events]
              [plugins.item-browser.items.subs    :as items.subs]
              [x.app-core.api                     :refer [r]]
              [x.app-ui.api                       :as ui]))



;; -- Update item events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) item-changes
  ;
  ; @return (map)
  [db [_ browser-id item-id item-changes]]
  (let [items-path (r body.subs/get-body-prop db browser-id :items-path)
        item-dex   (r items.subs/get-item-dex db browser-id item-id)]
       (update-in db (conj items-path item-dex) merge item-changes)))

(defn revert-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (let [items-path  (r body.subs/get-body-prop     db browser-id :items-path)
        backup-item (r backup.subs/get-backup-item db browser-id item-id)
        item-dex    (r items.subs/get-item-dex     db browser-id item-id)]
       (assoc-in db (conj items-path item-dex) backup-item)))



;; -- Delete item events ------------------------------------------------------
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



;; -- Update item events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) item-changes
  ;
  ; @return (map)
  [db [_ browser-id item-id item-changes]]
  (as-> db % (r backup.events/backup-item! % browser-id item-id)
             (r items.events/disable-item! % browser-id item-id)
             (r apply-changes!             % browser-id item-id item-changes)))

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
