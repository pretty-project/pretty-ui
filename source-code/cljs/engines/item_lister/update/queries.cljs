
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.update.queries
    (:require [engines.item-lister.backup.subs :as backup.subs]
              [engines.item-lister.core.subs   :as core.subs]
              [engines.item-lister.update.subs :as update.subs]
              [re-frame.api                    :as r :refer [r]]))



;; -- Reorder items queries ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-reorder-items-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  ; {:items (maps in vector)}
  [db [_ lister-id]]
  (let [reordered-items (r core.subs/export-listed-items db lister-id)]
       (r core.subs/use-query-params db lister-id {:items reordered-items})))

(defn get-reorder-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (vector)
  [db [_ lister-id]]
  (let [mutation-name  (r update.subs/get-mutation-name    db lister-id :reorder-items!)
        mutation-props (r get-reorder-items-mutation-props db lister-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; -- Delete items queries ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-delete-items-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  ;
  ; @return (map)
  ; {:item-ids (strings in vector)}
  [db [_ lister-id item-ids]]
  (r core.subs/use-query-params db lister-id {:item-ids item-ids}))

(defn get-delete-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  ;
  ; @return (vector)
  [db [_ lister-id item-ids]]
  (let [mutation-name  (r update.subs/get-mutation-name   db lister-id :delete-items!)
        mutation-props (r get-delete-items-mutation-props db lister-id item-ids)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; -- Undo delete items queries -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-undo-delete-items-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  ;
  ; @return (map)
  ; {:items (namespaced maps in vector)}
  [db [_ lister-id item-ids]]
  (let [exported-items (r backup.subs/export-backup-items db lister-id item-ids)]
       (r core.subs/use-query-params db lister-id {:items exported-items})))

(defn get-undo-delete-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  ;
  ; @return (vector)
  [db [_ lister-id item-ids]]
  (let [mutation-name  (r update.subs/get-mutation-name        db lister-id :undo-delete-items!)
        mutation-props (r get-undo-delete-items-mutation-props db lister-id item-ids)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; -- Duplicate items queries -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-duplicate-items-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  ;
  ; @return (map)
  ; {:item-ids (strings in vector)}
  [db [_ lister-id item-ids]]
  (r core.subs/use-query-params db lister-id {:item-ids item-ids}))

(defn get-duplicate-items-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (strings in vector) item-ids
  ;
  ; @return (vector)
  [db [_ lister-id item-ids]]
  (let [mutation-name  (r update.subs/get-mutation-name      db lister-id :duplicate-items!)
        mutation-props (r get-duplicate-items-mutation-props db lister-id item-ids)]
       [`(~(symbol mutation-name) ~mutation-props)]))
