
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
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (as-> db % (r backup.events/backup-current-item! % editor-id)
             (r ui/fake-process!                   % 15)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; - Az elem sikeres törlése után szükséges az elem kliens-oldali változtatásait eltárolni (local-changes)!
  ; - A kitörölt elem esetleges visszaállításakor a szerver számára az elem eredeti változatát szükséges elküldeni!
  ; - A kitörölt elem sikeres visszaállítása után a szerkesztő megnyitásakor az elem kliens-oldali változtatásait
  ;   szükséges alkalmazni az eredeti dokumentumon!
  (as-> db % (r backup.events/store-local-changes! % editor-id)
             (assoc-in % [:plugins :plugin-handler/meta-items editor-id :item-deleted?] true)))
