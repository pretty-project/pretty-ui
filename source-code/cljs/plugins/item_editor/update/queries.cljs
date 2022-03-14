
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.update.queries
    (:require [plugins.item-editor.backup.subs :as backup.subs]
              [plugins.item-editor.core.subs   :as core.subs]
              [plugins.item-editor.update.subs :as update.subs]
              [x.app-core.api                  :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-save-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (let [mutation-name (r update.subs/get-mutation-name db extension-id item-namespace :save)
        exported-item (r core.subs/export-current-item db extension-id item-namespace)]
       [:debug `(~(symbol mutation-name) ~{:item exported-item})]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (let [mutation-name   (r update.subs/get-mutation-name db extension-id item-namespace :delete)
        current-item-id (r core.subs/get-current-item-id db extension-id item-namespace)]
       [:debug `(~(symbol mutation-name) ~{:item-id current-item-id})]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-undo-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-id]]
  ; A törlés művelet visszavonásához szükséges a szerver számára elküldeni az elem másolatát,
  ; mivel feltételezhető, hogy az elem már nem elérhető a szerveren.
  (let [mutation-name (r update.subs/get-mutation-name  db extension-id item-namespace :undo-delete)
        backup-item   (r backup.subs/export-backup-item db extension-id item-namespace item-id)]
       [:debug `(~(symbol mutation-name) ~{:item backup-item})]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-duplicate-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  ; Duplikáláskor az elem AKTUÁLIS változatáról készül másolat a szerveren, amihez
  ; szükséges a szerver számára elküldeni az elem kliens-oldali – esetleges változtatásokat
  ; tartalmazó – aktuális változatát.
  (let [mutation-name (r update.subs/get-mutation-name db extension-id item-namespace :duplicate)
        exported-item (r core.subs/export-copy-item    db extension-id item-namespace)]
       [:debug `(~(symbol mutation-name) ~{:item exported-item})]))
