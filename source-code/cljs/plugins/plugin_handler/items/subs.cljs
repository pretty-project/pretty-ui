
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.items.subs
    (:require [plugins.plugin-handler.core.subs     :as core.subs]
              [plugins.plugin-handler.transfer.subs :as transfer.subs]
              [x.app-core.api                       :refer [r]]
              [x.app-db.api                         :as db]))



;; -- Single-item subscriptions -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ plugin-id item-id]]
  (letfn [(f [{:keys [id] :as item}] (if (= id item-id) item))]
         (let [downloaded-items (r core.subs/get-downloaded-items db plugin-id)]
              (some f downloaded-items))))

(defn export-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [db [_ plugin-id item-id]]
  (let [item-namespace (r transfer.subs/get-transfer-item db plugin-id :item-namespace)
        item           (r get-item                        db plugin-id item-id)]
       (db/document->namespaced-document item item-namespace)))

(defn item-downloaded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) item-id
  ;
  ; @return (boolean)
  [db [_ plugin-id item-id]]
  (let [item (r get-item db plugin-id item-id)]
       (boolean item)))
