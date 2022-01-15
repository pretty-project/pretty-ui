
(ns server-extensions.storage.capacity-handler
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.validator :as validator]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [x.server-core.api    :as a]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [server-extensions.storage.engine      :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-capacity-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:max-upload-size (B)
  ;   :total-capacity (B)
  ;   :used-capacity (B)}
  []
  (if-let [root-directory-document (mongo-db/get-document-by-id "storage" engine/ROOT-DIRECTORY-ID)]
          (if-let [server-config (a/subscribed [:core/get-server-config])]
                  {:max-upload-size (get server-config           :max-upload-size)
                   :total-capacity  (get server-config           :storage-capacity)
                   :used-capacity   (get root-directory-document :media/content-size)})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-capacity-details-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:max-upload-size (B)
  ;   :total-capacity (B)
  ;   :used-capacity (B)}
  [env response-props]
  (if-let [capacity-details (get-capacity-details)]
          (validator/validate-data capacity-details)))

(defresolver download-capacity-details
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:storage/download-capacity-details (map)
             ;    {:max-upload-size (B)
             ;     :total-capacity (B)
             ;     :used-capacity (B)}}
             [env resolver-props]
             {:storage/download-capacity-details (download-capacity-details-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [download-capacity-details])

(pathom/reg-handlers! ::handlers HANDLERS)
