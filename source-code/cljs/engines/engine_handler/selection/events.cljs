
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.selection.events
    (:require [candy.api                             :refer [return]]
              [engines.engine-handler.core.subs      :as core.subs]
              [engines.engine-handler.items.events   :as items.events]
              [engines.engine-handler.selection.subs :as selection.subs]
              [map.api                               :refer [dissoc-in]]
              [re-frame.api                          :refer [r]]
              [vector.api                            :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  (let [listed-items    (r core.subs/get-listed-items db engine-id)
        item-selections (vector/->items listed-items :id)]
       (assoc-in db [:engines :engine-handler/meta-items engine-id :selected-items] item-selections)))

(defn select-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ engine-id item-id]]
  (update-in db [:engines :engine-handler/meta-items engine-id :selected-items] vector/conj-item-once item-id))

(defn toggle-item-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ engine-id item-id]]
  (update-in db [:engines :engine-handler/meta-items engine-id :selected-items] vector/toggle-item item-id))

(defn toggle-single-item-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ engine-id item-id]]
  ; For those cases when the selection can contains only one item ...
  (let [selected-items (get-in db [:engines :engine-handler/meta-items engine-id :selected-items])]
       (if (= selected-items [item-id])
           (dissoc-in db [:engines :engine-handler/meta-items engine-id :selected-items])
           (assoc-in  db [:engines :engine-handler/meta-items engine-id :selected-items] [item-id]))))

(defn toggle-limited-item-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) item-id
  ; @param (integer) selection-limit
  ;
  ; @return (map)
  [db [_ engine-id item-id selection-limit]]
  ; For those cases when the selection can contains only limited number of items ...
  (let [selected-items      (get-in db [:engines :engine-handler/meta-items engine-id :selected-items])
        selected-item-count (r selection.subs/get-selected-item-count db engine-id)]
       (if (< selected-item-count selection-limit)
           (r toggle-item-selection! db engine-id item-id)
           (return db))))

(defn discard-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  (dissoc-in db [:engines :engine-handler/meta-items engine-id :selected-items]))

(defn disable-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  (let [selected-items (r core.subs/get-meta-item db engine-id :selected-items)]
       (r items.events/disable-items! db engine-id selected-items)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (strings in vector) selected-item-ids
  ;
  ; @return (map)
  [db [_ engine-id selected-item-ids]]
  (assoc-in db [:engines :engine-handler/meta-items engine-id :selected-items] selected-item-ids))

(defn import-single-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (string) selected-item-id
  ;
  ; @return (map)
  [db [_ engine-id selected-item-id]]
  (assoc-in db [:engines :engine-handler/meta-items engine-id :selected-items] [selected-item-id]))

(defn import-limited-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (strings in vector) selected-item-ids
  ; @param (integer) selection-limit
  ;
  ; @return (map)
  [db [_ engine-id selected-item-ids selection-limit]]
  (assoc-in db [:engines :engine-handler/meta-items engine-id :selected-items]
               (vector/trim selected-item-ids selection-limit)))
