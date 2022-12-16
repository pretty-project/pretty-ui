
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.update.events
    (:require [engines.item-lister.backup.events    :as backup.events]
              [engines.item-lister.body.subs        :as body.subs]
              [engines.item-lister.core.events      :as core.events]
              [engines.item-lister.core.subs        :as core.subs]
              [engines.item-lister.items.events     :as items.events]
              [engines.item-lister.selection.events :as selection.events]
              [re-frame.api                         :refer [r]]
              [x.ui.api                             :as x.ui]))



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
