
(ns server-extensions.storage.file-handler
    (:require [mid-fruits.candy    :refer [param return]]
              [mongo-db.api        :as mongo-db]
              [pathom.api          :as pathom]
              [server-fruits.http  :as http]
              [server-fruits.image :as image]
              [server-fruits.io    :as io]
              [x.server-core.api   :as a]
              [x.server-media.api  :as media]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [server-extensions.storage.engine      :as engine]))



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
  ;  {:destination-id (string)}
  ; @param (map) file-data
  ;
  ; @return (namespaced map)
  [env {:keys [destination-id]} {:keys [file-path filename size tempfile]}]
  (let [file-id            (mongo-db/generate-id)
        generated-filename (file-id->filename file-id filename)
        filepath           (media/filename->media-storage-filepath generated-filename)
        file-item {:media/alias filename :media/filename generated-filename :media/filesize size :media/id file-id
                   :media/path file-path :description ""}]
       (if (engine/attach-media-item! env destination-id file-id)
           (if-let [file-item (engine/insert-media-item! env file-item)]
                   ; Copy the temporary file to storage, and delete the temporary file
                   (do (io/copy-file!   tempfile filepath)
                       (io/delete-file! tempfile)
                       (image/make-image filepath (str filepath "x"))
                       (return file-item))))))

(defn- upload-files-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;
  ; @return (namespaced map)
  [{:keys [request] :as env} {:keys [destination-id] :as mutation-props}]
  (if-let [destination-item (mongo-db/get-document-by-id "storage" destination-id)]
          (let [destination-path (get  destination-item :media/path)
                file-path        (conj destination-path {:media/id destination-id})
                files-data (request->files-data request)]
               (letfn [(f [o _ file-data]
                          (let [file-data (assoc file-data :file-path file-path)]
                               (conj o (upload-file-f env mutation-props file-data))))]
                      (reduce-kv f [] files-data)))))

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
