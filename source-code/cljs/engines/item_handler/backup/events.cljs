
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.backup.events
    (:require [candy.api                            :refer [return]]
              [engines.engine-handler.backup.events :as backup.events]
              [engines.item-handler.backup.subs     :as backup.subs]
              [engines.item-handler.body.subs       :as body.subs]
              [engines.item-handler.core.subs       :as core.subs]
              [forms.api                            :as forms]
              [re-frame.api                         :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.backup.events
(def backup-current-item!       backup.events/backup-current-item!)
(def clean-backup-item!         backup.events/clean-backup-item!)
(def clean-current-item-backup! backup.events/clean-current-item-backup!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn revert-current-item!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ; (r revert-current-item! db handler-id)
  ;
  ; @return (map)
  [db [_ handler-id]]
  ; A revert-current-item! függvény visszaállítja az aktuálisan kezelt elemet az
  ; utoljára eltárolt másolat alapján.
  (let [current-item-id   (r core.subs/get-current-item-id   db handler-id)
        current-item-path (r core.subs/get-current-item-path db handler-id)
        backup-item       (r backup.subs/get-backup-item     db handler-id current-item-id)]
       (assoc-in db current-item-path backup-item)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn current-item-changed
  ; @description
  ; Checks whether the item really changed, if yes the function stores the
  ; {:changed? true} meta-item in the item.
  ;
  ; @param (keyword) handler-id
  ;
  ; @usage
  ; (r current-item-changed! db handler-id)
  ;
  ; @return (map)
  [db [_ handler-id]]
  ; XXX#6000 (source-code/cljc/iso/forms/helpers.cljc)
  ; XXX#6001 (source-code/cljc/iso/forms/helpers.cljc)
  (let [current-item-id (r core.subs/get-current-item-id db handler-id)
        current-item    (r core.subs/get-current-item    db handler-id)
        backup-item     (r backup.subs/get-backup-item   db handler-id current-item-id)
        items-path      (r body.subs/get-body-prop       db handler-id :items-path)]
       (if (forms/items-different? current-item backup-item)
           (assoc-in db (conj items-path current-item-id :meta-items :changed?) true)
           (return   db))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
; [:item-handler/revert-current-item! :my-handler]
(r/reg-event-db :item-handler/revert-current-item! revert-current-item!)

; @usage
; [:item-handler/current-item-changed :my-handler]
(r/reg-event-db :item-handler/current-item-changed current-item-changed)
