
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.8
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.queries
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]
              [app-plugins.item-editor.engine :as engine]
              [app-plugins.item-editor.subs   :as subs]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  [:debug (if (r subs/download-item? db extension-id item-namespace)
              ; If download item ...
              (let [resolver-id     (engine/resolver-id extension-id item-namespace :get)
                    current-item-id (r subs/get-current-item-id db extension-id)]
                  `(~resolver-id ~{:item-id current-item-id})))
          (if (r subs/download-suggestions? db extension-id item-namespace)
              ; If download suggestions ...
              (let [suggestion-keys (r subs/get-meta-item db extension-id item-namespace :suggestion-keys)]
                  `(:item-editor/get-item-suggestions {:suggestion-keys ~suggestion-keys
                                                       :extension-id    ~extension-id
                                                       :item-namespace  ~item-namespace})))])

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
       [:debug `(~(symbol mutation-name) ~exported-item)]))

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
       [:debug `(~(symbol mutation-name) ~{:item-id current-item-id})]))

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
       [:debug `(~(symbol mutation-name) ~backup-item)]))

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
  (let [mutation-name (engine/mutation-name       extension-id item-namespace :duplicate)
        exported-item (r subs/export-copy-item db extension-id item-namespace)]
       [:debug `(~(symbol mutation-name) ~exported-item)]))
