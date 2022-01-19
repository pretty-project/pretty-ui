
(ns app-extensions.storage.capacity-handler
    (:require [x.app-core.api :as a :refer [r]]
              [app-extensions.storage.engine :as engine]
              [app-plugins.item-browser.api  :as item-browser]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-storage-used-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (let [current-item-id (r item-browser/get-current-item-id db :storage)]
       (get-in db [:storage :item-browser/data-items current-item-id :used-capacity])))

(defn get-storage-total-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (let [current-item-id (r item-browser/get-current-item-id db :storage)]
       (get-in db [:storage :item-browser/data-items current-item-id :total-capacity])))

(defn get-storage-free-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (let [used-capacity  (r get-storage-used-capacity  db)
        total-capacity (r get-storage-total-capacity db)]
       (- total-capacity used-capacity)))

(defn get-max-upload-size
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (let [current-item-id (r item-browser/get-current-item-id db :storage)]
       (get-in db [:storage :item-browser/data-items current-item-id :max-upload-size])))
