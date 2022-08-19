

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.update.events
    (:require [plugins.item-viewer.backup.events :as backup.events]
              [plugins.item-viewer.core.subs     :as core.subs]
              [x.app-core.api                    :refer [r]]
              [x.app-ui.api                      :as ui]))



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
       (as-> db % (r backup.events/backup-current-item! % viewer-id current-item-id)
                  (r ui/fake-process!                   % 15))))
