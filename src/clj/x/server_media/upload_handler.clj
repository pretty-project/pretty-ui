
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.25
; Description:
; Version: v0.8.6
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.upload-handler
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.random  :as random]
              [local-db.api       :as local-db]
              [server-fruits.http :as http]
              [server-fruits.io   :as io]
              [pathom.api         :as pathom]
              [x.server-core.api  :as a]
              [x.server-db.api    :as db]
              [x.server-media.engine                 :as engine]
              [x.server-media.thumbnail-handler      :as thumbnail-handler]
              [com.wsscode.pathom3.connect.operation :as pathom.co]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn process-files-data
  ; @param (map) files-data
  ;
  ; @example
  ;  (process-files-data {"0" {:tempfile #object[java.io.File 0x4571e67a "/my-path/my-tempfile.tmp"]}})
  ;  =>
  ;  {"0" {:tempfile "/my-path/my-tempfile.tmp"}}
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

(defn request->files-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (let [params (http/request->params request)]
       (dissoc params :query)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (let [files-data               (request->files-data request)
        destination-directory-id (http/request->multipart-param request "destination-directory-id")
        response-query           (http/request->multipart-param request "response-query")
        processed-files-data     (process-files-data files-data)]))

;


;; -- Upload functions --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- upload-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) action-props
  ;  {:destination-directory-id (string)
  ;   :filename (string)
  ;   :filesize (B)
  ;   :temp-filepath (string)}
  ;
  ; @return (string)
  [env {:keys [destination-directory-id filename filesize temp-filepath]}]
  (let [file-id              (random/generate-string)
        generated-filename   (engine/filename->generated-filename     filename file-id)
        destination-filepath (engine/filename->media-storage-filepath generated-filename)
        file-document {:alias filename :filename generated-filename :filesize filesize}
        file-document (a/sub-prot env [destination-directory-id file-id file-document]
                                  engine/file-document-prototype)
        file-link     (db/document-id->document-link file-id :file)]

       ; Copy the temporary file to storage
       (io/copy-file! temp-filepath destination-filepath)

       ; Add file link to the destination-directory document
       (engine/attach-item! env {:destination-directory-id destination-directory-id
                                 :selected-item            file-link})

       ; Add the file document to the "files" collection
       (local-db/add-document! "files" file-document)

       ; Update the ancestor directories content-size
       (engine/update-path-content-size! destination-directory-id filesize +)

       ; Delete the temporary file
       (io/delete-file! temp-filepath)

       ; Generate file thumbnail
       ;(thumbnail-handler/generate-thumbnail! file-id)

       (return "File uploaded")))

(pathom.co/defmutation upload-files!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request
  ;   {:params (map)
  ;    {:query (string)
  ;     "0" (map)
  ;      {:content-type (string)
  ;       :filename (string)
  ;       :size (B)
  ;       :tempfile (string)}
  ;     "1" (map)
  ;     "2" (map)
  ;     ...}}}
  ; @param (map) mutation-props
  ;  {:destination-directory-id (string)}
  ;
  ; @return (string)
  [{:keys [request] :as env} {:keys [destination-directory-id]}]
  {::pathom.co/op-name 'media/upload-files!}

  (let [files-data           (request->files-data request)
        processed-files-data (process-files-data  files-data)]
       (doseq [[_ {:keys [filename size tempfile] :as file}] processed-files-data]
              (let [action-props {:destination-directory-id destination-directory-id
                                  :filename                 filename
                                  :filesize                 size
                                  :temp-filepath            tempfile}]
                   (upload-file! env action-props))))

  (return "Files uploaded"))

(pathom/reg-handler! :upload-files upload-files!)
