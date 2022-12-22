
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.update.queries
    (:require [engines.item-handler.backup.subs :as backup.subs]
              [engines.item-handler.core.subs   :as core.subs]
              [engines.item-handler.update.subs :as update.subs]
              [re-frame.api                     :refer [r]]))



;; -- Save item queries -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-save-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  ; {:item (namespaced map)}
  [db [_ handler-id]]
  (let [exported-item (r core.subs/export-current-item db handler-id)]
       (r core.subs/use-query-params db handler-id {:item exported-item})))

(defn get-save-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (let [new-item?      (r core.subs/new-item? db handler-id)
        action-key     (if new-item? :add-item! :save-item!)
        mutation-name  (r update.subs/get-mutation-name db handler-id action-key)
        mutation-props (r get-save-item-mutation-props  db handler-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; -- Delete item queries -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-delete-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  ; {:item-id (string)}
  [db [_ handler-id]]
  (let [current-item-id (r core.subs/get-current-item-id db handler-id)]
       (r core.subs/use-query-params db handler-id {:item-id current-item-id})))

(defn get-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (let [mutation-name  (r update.subs/get-mutation-name  db handler-id :delete-item!)
        mutation-props (r get-delete-item-mutation-props db handler-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; -- Undo delete item queries ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-undo-delete-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ;
  ; @return (map)
  ; {:item (namespaced map)}
  [db [_ handler-id item-id]]
  (let [backup-item (r backup.subs/export-backup-item db handler-id item-id)]
       (r core.subs/use-query-params db handler-id {:item backup-item})))

(defn get-undo-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ handler-id item-id]]
  ; A törlés művelet visszavonásához szükséges a szerver számára elküldeni az elem másolatát,
  ; mivel feltételezhető, hogy az elem már nem elérhető a szerveren.
  (let [mutation-name  (r update.subs/get-mutation-name       db handler-id :undo-delete-item!)
        mutation-props (r get-undo-delete-item-mutation-props db handler-id item-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; -- Duplicate item queries --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-duplicate-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  ; {:item-id (string)}
  [db [_ handler-id]]
  (let [current-item-id (r core.subs/get-current-item-id db handler-id)]
       (r core.subs/use-query-params db handler-id {:item-id current-item-id})))

(defn get-duplicate-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (let [mutation-name  (r update.subs/get-mutation-name     db handler-id :duplicate-item!)
        mutation-props (r get-duplicate-item-mutation-props db handler-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))
