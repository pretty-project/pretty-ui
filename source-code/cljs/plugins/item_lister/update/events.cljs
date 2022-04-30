
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.update.events
    (:require [plugins.item-lister.backup.events :as backup.events]
              [plugins.item-lister.core.events   :as core.events]
              [plugins.item-lister.items.events  :as items.events]
              [x.app-core.api                    :refer [r]]
              [x.app-ui.api                      :as ui]))



;; -- Delete items events -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (as-> db % (r backup.events/backup-selected-items! % lister-id)
             (r items.events/disable-selected-items! % lister-id)
             (r ui/fake-process!                     % 15)))

(defn delete-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (as-> db % (r core.events/quit-actions-mode! % lister-id)
             (r items.events/enable-all-items! % lister-id)))



;; -- Duplicate items events --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (r core.events/quit-actions-mode! db lister-id))
