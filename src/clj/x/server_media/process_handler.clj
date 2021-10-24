
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.21
; Description:
; Version: v0.3.6
; Compatibility: x4.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.process-handler
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.eql     :as eql]
              [mid-fruits.vector  :as vector]
              [pathom.api         :as pathom :refer [ENVIRONMENT]]
              [server-fruits.http :as http]
              [x.server-user.api  :as user]))



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
      (let [query       (pathom/request->query request)
            environment (assoc @ENVIRONMENT :request request)]
           (pathom/process-query! environment query))
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
      (let [environment              (assoc @ENVIRONMENT :request request)
            files-data               (request->files-data request)
            destination-directory-id (http/request->multipart-param request "destination-directory-id")
            response-query           (http/request->multipart-param request "response-query")
            processed-files-data     (process-files-data files-data)
            mutation-props           {:destination-directory-id destination-directory-id
                                      :processed-files-data     processed-files-data}
            query-action            `(media/upload-files! ~mutation-props)
            query                    (eql/append-to-query response-query query-action)]
           (pathom/process-query! (param environment)
                                  (pathom/read-query query)))
      (http/error-wrap {:error-message :permission-denied :status 401})))
