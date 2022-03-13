
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.backup.events
    (:require [plugins.item-browser.items.subs :as items.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn backup-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (let [backup-item (r items.subs/get-item db extension-id item-namespace item-id)]
       (assoc-in db [extension-id :item-browser/backup-items item-id] backup-item)))
