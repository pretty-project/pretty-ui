
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.2
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.queries
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-core.api       :as a :refer [r]]
              [app-plugins.item-editor.engine :as engine]
              [app-plugins.item-editor.subs   :as subs]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-download-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  [(if (r subs/download-item? db extension-id item-namespace)
       ; If download item ...
       (let [current-item-entity (r subs/get-current-item-entity db extension-id item-namespace)
             added-at-key        (keyword item-namespace "added-at")]
            {current-item-entity [added-at-key '*]}))
   (if (r subs/download-suggestions? db extension-id item-namespace)
       ; If download suggestions ...
       (let [suggestion-keys (r subs/get-meta-value db extension-id item-namespace :suggestion-keys)
             resolver-id     (engine/resolver-id       extension-id item-namespace :suggestions)]
           `(~resolver-id {:suggestion-keys ~suggestion-keys})))])

(defn get-mark-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace mark-props]]
        ; XXX#4460
        ; Az item-lister pluginnal megegyezően az item-editor plugin is a {:toggle-f ...}
        ; tulajdonságként átadott függvény használatával jelöli meg az elemeket.
  (let [mutation-name (engine/mutation-name         extension-id item-namespace :merge)
        exported-item (r subs/export-marked-item db extension-id item-namespace mark-props)]
       [`(~(symbol mutation-name) ~exported-item)]))

(defn get-save-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (let [mutation-name (engine/mutation-name          extension-id item-namespace :save)
        exported-item (r subs/export-current-item db extension-id item-namespace)]
       [`(~(symbol mutation-name) ~exported-item)]))

(defn get-delete-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (let [mutation-name   (engine/mutation-name          extension-id item-namespace :delete)
        current-item-id (r subs/get-current-item-id db extension-id)]
       [`(~(symbol mutation-name) ~{:item-id current-item-id})]))

(defn get-undo-delete-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace item-id]]
  (let [mutation-name (engine/mutation-name         extension-id item-namespace :undo-delete)
        backup-item   (r subs/export-backup-item db extension-id item-namespace item-id)]
       [`(~(symbol mutation-name) ~backup-item)]))

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
  (let [mutation-name (engine/mutation-name          extension-id item-namespace :duplicate)
        exported-item (r subs/export-current-item db extension-id item-namespace)]
       [`(~(symbol mutation-name) ~exported-item)]))
