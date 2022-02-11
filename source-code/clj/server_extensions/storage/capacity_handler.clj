
(ns server-extensions.storage.capacity-handler
    (:require [mid-fruits.candy  :refer [param return]]
              [mongo-db.api      :as mongo-db]
              [x.server-core.api :as a]
              [server-extensions.storage.engine :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-capacity-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [root-directory-document (mongo-db/get-document-by-id "storage" engine/ROOT-DIRECTORY-ID)]
          (if-let [server-config @(a/subscribe [:core/get-server-config])]
                  {:media/max-upload-size (get server-config           :max-upload-size)
                   :media/total-capacity  (get server-config           :storage-capacity)
                   :media/used-capacity   (get root-directory-document :media/content-size)})))
