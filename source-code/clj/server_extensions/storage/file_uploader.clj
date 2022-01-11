
(ns server-extensions.storage.file-uploader
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.random  :as random]
              [mongo-db.api       :as mongo-db]
              [pathom.api         :as pathom]
              [prototypes.api     :as prototypes]
              [server-fruits.http :as http]
              [server-fruits.io   :as io]
              [x.server-core.api  :as a]
              [x.server-media.api :as media]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->files-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:params (map)
  ;    {:query (string)}}
  ;
  ; @example
  ;  (request->files-data {:params {"0" {:tempfile #object[java.io.File 0x4571e67a "/my-tempfile.tmp"}
  ;                                 "1" {:tempfile #object[java.io.File 0x4571e67a "/your-tempfile.tmp"}
  ;                                 :query [...]}})
  ;  =>
  ;  {"0" {:tempfile "/my-tempfile.tmp"}
  ;   "1" {:tempfile "/your-tempfile.tmp"}}
  ;
  ; @return (map)
  [request]
  (let [params (http/request->params request)]
       (letfn [(f [o k v] (assoc o k (update v :tempfile str)))]
              (reduce-kv f {} (dissoc params :query)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-id->filename
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) file-id
  ; @param (string) filename
  ;
  ; @return (string)
  [file-id filename]
  (if-let [extension (io/filename->extension filename)]
          (str    file-id "." extension)
          (return file-id)))

(defn- upload-file-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:directory-id (string)}
  ; @param (map) file-data
  ;
  ; @return (namespaced map)
  [env {:keys [directory-id]} {:keys [filename size tempfile]}]
  (let [file-id            (random/generate-string)
        generated-filename (file-id->filename file-id filename)
        filepath           (media/filename->media-storage-filepath generated-filename)
        file-item {:file/alias filename :filename generated-filename :file/filesize size :file/id file-id}]
       (println (str "save-x : " (mongo-db/save-document! "directories" {:x/a :aaa})))))
       ; Copy the temporary file to storage, and delete the temporary file
       ;(io/copy-file!   tempfile filepath)
       ;(io/delete-file! tempfile)))

(defn- upload-files-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;
  ; @return (namespaced map)
  [{:keys [request] :as env} mutation-props]
  (let [files-data (request->files-data request)]
       (doseq [[_ file-data] files-data]
              (upload-file-f env mutation-props file-data))))

(defmutation upload-files!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (?)
             [env mutation-props]
             {::pathom.co/op-name 'storage/upload-files!}
             (upload-files-f env mutation-props))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [upload-files!])

(pathom/reg-handlers! ::handlers HANDLERS)
