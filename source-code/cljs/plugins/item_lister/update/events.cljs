
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
              [plugins.item-lister.body.subs        :as body.subs]
              [plugins.item-lister.core.events      :as core.events]
              [plugins.item-lister.core.subs        :as core.subs]
              [plugins.item-lister.items.events     :as items.events]
              [plugins.item-lister.selection.events :as selection.events]
              [re-frame.api                         :refer [r]]
              [x.app-ui.api                         :as x.ui]))



;; -- Reorder items events ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reorder-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (maps in vector) reordered-items
  ;
  ; @return (map)
  [db [_ lister-id reordered-items]]
  (let [items-path       (r body.subs/get-body-prop        db lister-id :items-path)
        order-key        (r body.subs/get-body-prop        db lister-id :order-key)
        downloaded-items (r core.subs/get-downloaded-items db lister-id)]
       (letfn [(f [%1 %2 %3] (conj %1 (assoc %3 order-key (get-in downloaded-items [%2 order-key]))))]
              (let [updated-items (reduce-kv f [] reordered-items)]
                   (assoc-in db items-path updated-items)))))



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
             (r x.ui/fake-process!                       % 15)))

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
