
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.capacity-handler.subs
    (:require [plugins.item-browser.api :as item-browser]
              [x.app-core.api           :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-used-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (let [current-item-id (r item-browser/get-current-item-id db :storage)]
       (get-in db [:storage :item-browser/data-items current-item-id :used-capacity])))

(defn get-total-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (let [current-item-id (r item-browser/get-current-item-id db :storage)]
       (get-in db [:storage :item-browser/data-items current-item-id :total-capacity])))

(defn get-free-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (let [used-capacity  (r get-used-capacity  db)
        total-capacity (r get-total-capacity db)]
       (- total-capacity used-capacity)))

(defn get-max-upload-size
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (let [current-item-id (r item-browser/get-current-item-id db :storage)]
       (get-in db [:storage :item-browser/data-items current-item-id :max-upload-size])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.capacity-handler/get-free-capacity get-free-capacity)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.capacity-handler/get-max-upload-size get-max-upload-size)
