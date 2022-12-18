
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.update.events
    (:require [engines.item-viewer.backup.events :as backup.events]
              [engines.item-viewer.core.subs     :as core.subs]
              [re-frame.api                      :refer [r]]))



;; -- Delete item events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (map)
  [db [_ viewer-id]]
  ; Az elem törlése előtt szükséges az elemről másolatot készíteni a kitörölt elem esetleges
  ; visszaállításához.
  (let [current-item-id (r core.subs/get-current-item-id db viewer-id)]
       (r backup.events/backup-current-item! db viewer-id current-item-id)))
