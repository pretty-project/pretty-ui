
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.items.subs
    (:require [mid-fruits.loop                    :refer [some-indexed]]
              [plugins.item-browser.download.subs :as download.subs]
              [plugins.item-lister.items.subs     :as plugins.item-lister.items.subs]
              [x.app-core.api                     :refer [r]]
              [x.app-db.api                       :as db]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.items.subs
(def toggle-item-selection? plugins.item-lister.items.subs/toggle-item-selection?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-id]]
  (letfn [(f [{:keys [id] :as item}] (if (= id item-id) item))]
         (let [downloaded-items (r download.subs/get-downloaded-items db extension-id item-namespace)]
              (some f downloaded-items))))

(defn get-item-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (integer)
  [db [_ extension-id item-namespace item-id]]
  (letfn [(f [item-dex {:keys [id]}] (if (= id item-id) item-dex))]
         (let [downloaded-items (r download.subs/get-downloaded-items db extension-id item-namespace)]
              (some-indexed f downloaded-items))))

(defn export-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [db [_ extension-id item-namespace item-id]]
  (let [item (r get-item db extension-id item-namespace item-id)]
       (db/document->namespaced-document item item-namespace)))
