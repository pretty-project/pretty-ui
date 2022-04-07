
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.capacity-handler.subs
    (:require [plugins.item-browser.api :as item-browser]
              [x.app-core.api           :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-max-upload-size
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (or (get-in db [:storage :media-selector/browsed-item :max-upload-size])
      (get-in db [:storage :media-browser/browsed-item  :max-upload-size])))

(defn get-used-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (or (get-in db [:storage :media-selector/browsed-item :used-capacity])
      (get-in db [:storage :media-browser/browsed-item  :used-capacity])))

(defn get-total-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (or (get-in db [:storage :media-selector/browsed-item :total-capacity])
      (get-in db [:storage :media-browser/browsed-item  :total-capacity])))

(defn get-free-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (let [used-capacity  (r get-used-capacity  db)
        total-capacity (r get-total-capacity db)]
       (- total-capacity used-capacity)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.capacity-handler/get-max-upload-size get-max-upload-size)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.capacity-handler/get-free-capacity get-free-capacity)
