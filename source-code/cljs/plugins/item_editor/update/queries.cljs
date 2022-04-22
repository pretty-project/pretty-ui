
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.update.queries
    (:require [plugins.item-editor.backup.subs :as backup.subs]
              [plugins.item-editor.core.subs   :as core.subs]
              [plugins.item-editor.update.subs :as update.subs]
              [x.app-core.api                  :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-save-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  ;  {:item (namespaced map)}
  [db [_ editor-id]]
  (let [exported-item (r core.subs/export-current-item db editor-id)]
       (merge (r core.subs/get-meta-item db editor-id :default-query-params)
              {:item exported-item})))

(defn get-save-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (vector)
  [db [_ editor-id]]
  (let [new-item?      (r core.subs/new-item?           db editor-id)
        mutation-name  (r update.subs/get-mutation-name db editor-id (if new-item? :add-item! :save-item!))
        mutation-props (r get-save-item-mutation-props  db editor-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-delete-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  ;  {:item-id (string)}
  [db [_ editor-id]]
  (let [current-item-id (r core.subs/get-current-item-id db editor-id)]
       (merge (r core.subs/get-meta-item db editor-id :default-query-params)
              {:item-id current-item-id})))

(defn get-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (vector)
  [db [_ editor-id]]
  (let [mutation-name  (r update.subs/get-mutation-name  db editor-id :delete-item!)
        mutation-props (r get-delete-item-mutation-props db editor-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-undo-delete-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @return (map)
  ;  {:item (namespaced map)}
  [db [_ editor-id item-id]]
  (let [backup-item (r backup.subs/export-backup-item db editor-id item-id)]
       (merge (r core.subs/get-meta-item db editor-id :default-query-params)
              {:item backup-item})))

(defn get-undo-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ editor-id item-id]]
  ; A törlés művelet visszavonásához szükséges a szerver számára elküldeni az elem másolatát,
  ; mivel feltételezhető, hogy az elem már nem elérhető a szerveren.
  (let [mutation-name  (r update.subs/get-mutation-name       db editor-id :undo-delete-item!)
        mutation-props (r get-undo-delete-item-mutation-props db editor-id item-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-duplicate-item-mutation-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  ;  {:item (namespaced map)}
  [db [_ editor-id]]
  (let [exported-item (r core.subs/export-current-item db editor-id)]
       (merge (r core.subs/get-meta-item db editor-id :default-query-params)
              {:item exported-item})))

(defn get-duplicate-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (vector)
  [db [_ editor-id]]
  ; Duplikáláskor az elem AKTUÁLIS változatáról készül másolat a szerveren, amihez
  ; szükséges a szerver számára elküldeni az elem kliens-oldali – esetleges változtatásokat
  ; tartalmazó – aktuális változatát.
  (let [mutation-name  (r update.subs/get-mutation-name     db editor-id :duplicate-item!)
        mutation-props (r get-duplicate-item-mutation-props db editor-id)]
       [`(~(symbol mutation-name) ~mutation-props)]))
