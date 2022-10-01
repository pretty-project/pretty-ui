
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.update.events
    (:require [plugins.item-lister.backup.events    :as backup.events]
              [plugins.item-lister.core.events      :as core.events]
              [plugins.item-lister.items.events     :as items.events]
              [plugins.item-lister.selection.events :as selection.events]
              [re-frame.api                         :refer [r]]
              [x.app-ui.api                         :as ui]))



;; -- Delete items events -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (as-> db % (r backup.events/backup-selected-items!     % lister-id)
             (r selection.events/disable-selected-items! % lister-id)
             (r ui/fake-process!                         % 15)))

(defn delete-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (r items.events/enable-all-items! db lister-id))



;; -- Duplicate items events --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicate-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]])
