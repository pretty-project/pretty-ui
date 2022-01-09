
(ns server-extensions.storage.capacity-handler
    (:require [mid-fruits.candy  :refer [param return]]
              [mongo-db.api      :as mongo-db]
              [pathom.api        :as pathom]
              [x.server-core.api :as a]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [server-extensions.storage.engine      :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-capacity-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:used-capacity (B)
  ;   :total-capacity (B)}
  []
  (if-let [root-directory-document (mongo-db/get-document-by-id "storage" engine/ROOT-DIRECTORY-ID)]
          (merge {:used-capacity (get root-directory-document :directory/content-size)}
                 (a/subscribed [:core/get-storage-details]))
          {:x :y}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-capacity-details-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:error (keyword)}
  [env response-props]
  (if-let [capacity-details (get-capacity-details)]
          (return capacity-details)
          {:error :xxx}))

(defresolver download-capacity-details
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:storage/download-capacity-details (map)
             [env resolver-props]
             {:storage/download-capacity-details (download-capacity-details-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [download-capacity-details])

(pathom/reg-handlers! ::handlers HANDLERS)
