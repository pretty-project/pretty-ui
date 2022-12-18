
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.update.events
    (:require [engines.item-browser.backup.events :as backup.events]
              [engines.item-browser.backup.subs   :as backup.subs]
              [engines.item-browser.body.subs     :as body.subs]
              [engines.item-browser.items.events  :as items.events]
              [engines.item-browser.items.subs    :as items.subs]
              [re-frame.api                       :as r :refer [r]]
              [x.ui.api                           :as x.ui]))



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
             (r x.ui/fake-progress!        % 15)))



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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-browser/revert-changes! revert-changes!)
