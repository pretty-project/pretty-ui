
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.capacity-handler.side-effects
    (:require [extensions.storage.config :as config]
              [mongo-db.api              :as mongo-db]
              [x.server-core.api         :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-max-upload-size
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  @(a/subscribe [:core/get-server-config-item :max-upload-size]))

(defn get-total-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  @(a/subscribe [:core/get-server-config-item :storage-capacity]))

(defn get-used-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [root-directory-document (mongo-db/get-document-by-id "storage" config/ROOT-DIRECTORY-ID)]
          (get root-directory-document :media/content-size)))

(defn get-free-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [used-capacity (get-used-capacity)]
          (if-let [total-capacity @(a/subscribe [:core/get-server-config-item :storage-capacity])]
                  (- total-capacity used-capacity))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-capacity-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  {:media/max-upload-size (get-max-upload-size)
   :media/total-capacity  (get-total-capacity)
   :media/used-capacity   (get-used-capacity)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn capacity-limit-exceeded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [content-size]
  (> content-size (get-free-capacity)))
