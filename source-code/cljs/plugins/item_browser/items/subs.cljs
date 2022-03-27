
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.items.subs
    (:require [mid-fruits.loop                    :refer [some-indexed]]
              [plugins.item-browser.core.subs     :as core.subs]
              [plugins.item-browser.transfer.subs :as transfer.subs]
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
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (letfn [(f [{:keys [id] :as item}] (if (= id item-id) item))]
         (let [downloaded-items (r core.subs/get-downloaded-items db browser-id)]
              (some f downloaded-items))))

(defn get-item-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (integer)
  [db [_ browser-id item-id]]
  (letfn [(f [item-dex {:keys [id]}] (if (= id item-id) item-dex))]
         (let [downloaded-items (r core.subs/get-downloaded-items db browser-id)]
              (some-indexed f downloaded-items))))

(defn export-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [db [_ browser-id item-id]]
  (let [item-namespace (r transfer.subs/get-transfer-item db browser-id :item-namespace)
        item           (r get-item                        db browser-id item-id)]
       (db/document->namespaced-document item item-namespace)))
