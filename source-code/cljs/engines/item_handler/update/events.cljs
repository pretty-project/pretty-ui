
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.update.events
    (:require [engines.item-handler.backup.events :as backup.events]
              [engines.item-handler.core.subs     :as core.subs]
              [re-frame.api                       :refer [r]]
              [x.ui.api                           :as x.ui]))



;; -- Delete item events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  ; Az elem törlése előtt szükséges az elemről másolatot készíteni a kitörölt elem esetleges
  ; visszaállításához.
  (let [current-item-id (r core.subs/get-current-item-id db handler-id)]
       (as-> db % (r backup.events/backup-current-item! % handler-id current-item-id)
                  (r x.ui/fake-progress!                % 15))))
