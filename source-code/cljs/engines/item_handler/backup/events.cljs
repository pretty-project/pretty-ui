
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.backup.events
    (:require [engines.engine-handler.backup.events :as backup.events]
              [engines.item-handler.backup.subs     :as backup.subs]
              [engines.item-handler.body.subs       :as body.subs]
              [engines.item-handler.core.subs       :as core.subs]
              [re-frame.api                         :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.backup.events
(def backup-current-item!       backup.events/backup-current-item!)
(def clean-backup-item!         backup.events/clean-backup-item!)
(def clean-current-item-backup! backup.events/clean-current-item-backup!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn revert-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  ; A revert-item! függvény visszaállítja az aktuálisan kezelt elemet az utoljára
  ; eltárolt másolat alapján.
  (let [item-path       (r body.subs/get-body-prop       db handler-id :item-path)
        current-item-id (r core.subs/get-current-item-id db handler-id)
        backup-item     (r backup.subs/get-backup-item   db handler-id current-item-id)]
       (assoc-in db item-path backup-item)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-handler/revert-item! revert-item!)
