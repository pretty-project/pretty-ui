
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.core.events
    (:require [candy.api                          :refer [return]]
              [engines.engine-handler.core.events :as core.events]
              [engines.item-handler.backup.events :as backup.events]
              [engines.item-handler.backup.subs   :as backup.subs]
              [engines.item-handler.body.subs     :as body.subs]
              [engines.item-handler.core.subs     :as core.subs]
              [map.api                            :as map :refer [dissoc-in]]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-item!  core.events/remove-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
(def set-mode!          core.events/set-mode!)
(def set-engine-error!  core.events/set-engine-error!)
(def set-item-id!       core.events/set-item-id!)
(def clear-item-id!     core.events/clear-item-id!)
(def derive-item-id!    core.events/derive-item-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  (let [current-item-path (r core.subs/get-current-item-path db handler-id)
        suggestions-path  (r body.subs/get-body-prop         db handler-id :suggestions-path)]
       (-> db (dissoc-in [:engines :engine-handler/meta-items handler-id :data-received?])
              (dissoc-in current-item-path)
              (dissoc-in suggestions-path))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-default-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  ; XXX#5067 (source-code/cljs/engines/item_handler/core/events.cljs)
  (let [default-item      (r body.subs/get-body-prop         db handler-id :default-item)
        current-item-path (r core.subs/get-current-item-path db handler-id)]
       (update-in db current-item-path map/reversed-merge default-item)))

(defn use-initial-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  (if (r core.subs/new-item? db handler-id)
      (let [initial-item      (r body.subs/get-body-prop         db handler-id :initial-item)
            current-item-path (r core.subs/get-current-item-path db handler-id)]
           (assoc-in db current-item-path initial-item))
      (return db)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-handler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  ; XXX#1309 (source-code/cljs/engines/item_handler/core/effects.cljs)
  ;
  ; XXX#1310
  ; If the current item is already downloaded (possibly by another engine),
  ; the item has to be backed up, because the 'backup-current-item!' function
  ; will not be applied by the 'receive-item!' function.
  (if (r backup.subs/current-item-backed-up? db handler-id)
      (return                                db)
      (r backup.events/backup-current-item!  db handler-id)))

(defn reload-handler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  ; XXX#1309 (source-code/cljs/engines/item_handler/core/effects.cljs)
  ; XXX#1310
  (if (r backup.subs/current-item-backed-up? db handler-id)
      (as-> db % (r remove-meta-item!                  % handler-id :engine-error))
      (as-> db % (r remove-meta-item!                  % handler-id :engine-error)
                 (r backup.events/backup-current-item! % handler-id))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-handler/set-engine-error! set-engine-error!)
