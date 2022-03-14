
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.update.events
    (:require [plugins.item-lister.backup.events :as backup.events]
              [plugins.item-lister.items.events  :as items.events]
              [x.app-core.api                    :refer [r]]
              [x.app-ui.api                      :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (as-> db % (r backup.events/backup-selected-items! % extension-id item-namespace)
             (r items.events/disable-selected-items! % extension-id item-namespace)
             (r ui/fake-process!                     % 15)))

(defn delete-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (as-> db % (r items.events/reset-selections! % extension-id item-namespace)
             (r items.events/enable-all-items! % extension-id item-namespace)))
