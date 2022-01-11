
(ns app-extensions.storage.capacity-handler
    (:require [x.app-core.api :as a :refer [r]]
              [app-extensions.storage.engine :as engine]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-storage-used-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (get-in db [:storage :capacity-handler/meta-items :used-capacity]))

(defn get-storage-total-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (get-in db [:storage :capacity-handler/meta-items :storage-capacity]))

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
  (get-in db [:storage :capacity-handler/meta-items :max-upload-size]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- receive-capacity-details!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-response
  ;  {:storage/download-capacity-details (map)}
  ;
  ; @return (map)
  [db [_ {:storage/keys [download-capacity-details]}]]
  (assoc-in db [:storage :capacity-handler/meta-items] download-capacity-details))
