
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.21
; Description:
; Version: v0.2.4
; Compatibility: x4.3.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.process-handler
    (:require [mid-fruits.candy            :refer [param]]
              [mid-fruits.vector           :as vector]
              [server-fruits.http          :as http]
              [x.server-media.item-handler :as item-handler]
              [x.server-user.api           :as user]
              [x.server-sync.api           :as sync]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
(def RESOLVERS [item-handler/get-directory-data
                item-handler/get-file-data])

; @constant (vector)
(def MUTATIONS [item-handler/update-directory!
                item-handler/update-file!
                item-handler/create-directory!
                item-handler/delete-items!
                item-handler/copy-items!
                item-handler/move-items!
                item-handler/upload-files!])

; @constant (vector)
(def HANDLERS (vector/concat-items RESOLVERS MUTATIONS))

; @constant (vector)
(def REGISTRY [HANDLERS])

; @constant (map)
(def ENVIRONMENT (sync/environment-register REGISTRY))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn process-files-data
  ; @param (map) files-data
  ;
  ; @example
  ;  (process-files-data {"0" {:tempfile #object[java.io.File 0x4571e67a "/my-path/my-tempfile.tmp"]}})
  ;  => {"0" {:tempfile "/my-path/my-tempfile.tmp"}})
  ;
  ; @return (map)
  [files-data]
  (reduce-kv (fn [result file-key file-data]
                 (let [processed-file-data (update file-data :tempfile str)]
                      (assoc result file-key processed-file-data)))
             (param {})
             (param files-data)))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn multipart-params->files-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) multipart-params
  ;
  ; @return (map)
  [multipart-params]
  (dissoc multipart-params "destination-directory-id" "response-query"))

(defn request->files-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (let [multipart-params (http/request->multipart-params request)]
       (multipart-params->files-data multipart-params)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn process-query!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (if (user/request->authenticated? request)
      (let [query       (sync/request->query request)
            environment (assoc ENVIRONMENT :request request)]
           (println "environment: " environment)
           (println "request: "  (str request))
           (println "query: "  (str query))
           (println "answer: " (str (sync/process-query! environment query)))
           (sync/process-query! environment query))
      (http/error-wrap {:error-message :permission-denied :status 401})))

(defn process-upload!
  ; @param (map) request
  ;  {:multipart-params (map)
  ;   {"destination-directory-id" (string)
  ;    "response-query" (string)
  ;    "0" (map)
  ;     {:content-type (string)
  ;      :filename (string)
  ;      :size (B)
  ;      :tempfile (string)}
  ;    "1" (map)
  ;    "2" (map)
  ;    ...}}
  ;
  ; @return (map)
  [request]
  (if (user/request->authenticated? request)
      (let [environment              (assoc ENVIRONMENT :request request)
            files-data               (request->files-data request)
            destination-directory-id (http/request->multipart-param request "destination-directory-id")
            response-query           (http/request->multipart-param request "response-query")
            processed-files-data     (process-files-data files-data)
            mutation-props           {:destination-directory-id destination-directory-id
                                      :processed-files-data     processed-files-data}
            query-action            `(media/upload-files! ~mutation-props)
            query                    (sync/append-to-query response-query query-action)]
           (sync/process-query! (param environment)
                                (sync/read-query query)))
      (http/error-wrap {:error-message :permission-denied :status 401})))
