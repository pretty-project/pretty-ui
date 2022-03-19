
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
  ; - Az elem sikeres törlése után szükséges az elem kliens-oldali változtatásait eltárolni (local-changes)!
  ; - A kitörölt elem esetleges visszaállításakor a szerver számára az elem eredeti változatát szükséges elküldeni!
  ; - A kitörölt elem sikeres visszaállítása után a szerkesztő megnyitásakor az elem kliens-oldali változtatásait
  ;   szükséges alkalmazni az eredeti dokumentumon!
  (as-> db % (r backup.events/store-local-changes! % extension-id item-namespace)
             (assoc-in % [:plugins :item-editor/meta-items extension-id :item-deleted?] true)))
