
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.update.events
    (:require [plugins.item-editor.backup.events :as backup.events]
              [x.app-core.api                    :refer [r]]
              [x.app-ui.api                      :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (as-> db % (r backup.events/backup-current-item! % extension-id item-namespace)
             (r ui/fake-process!                   % 15)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; Az elem sikeres törlése után az elem utolsó állapotáról másolat készül, ami alapján lehetséges
  ; visszaállítani az elemet annak törlésének visszavonása esemény esetleges megtörténtekor.
  (r backup.events/store-local-changes! db extension-id item-namespace))
