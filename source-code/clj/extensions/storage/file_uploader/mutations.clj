
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.file-uploader.mutations
    (:require [com.wsscode.pathom3.connect.operation            :as pathom.co :refer [defmutation]]
              [extensions.storage.capacity-handler.side-effects :as capacity-handler.side-effects]
              [extensions.storage.file-uploader.helpers         :as file-uploader.helpers]
              [extensions.storage.file-uploader.prototypes      :as file-uploader.prototypes]
              [extensions.storage.core.side-effects             :as core.side-effects]
              [mid-fruits.candy                                 :refer [return]]
              [mongo-db.api                                     :as mongo-db]
              [pathom.api                                       :as pathom]
              [server-fruits.io                                 :as io]
              [x.server-media.api                               :as media]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- upload-file-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:destination-id (string)}
  ; @param (map) file-data
  ;
  ; @return (namespaced map)
  [env {:keys [destination-id]} {:keys [tempfile] :as file-data}]
  (let [file-item (file-uploader.prototypes/file-item-prototype file-data)
        filename  (get file-item :media/filename)
        filepath  (media/filename->media-storage-filepath filename)]
       (if (core.side-effects/attach-item! env destination-id file-item)
           (when-let [file-item (core.side-effects/insert-item! env file-item)]
                     ; Copy the temporary file to storage, and delete the temporary file
                     (io/copy-file!   tempfile filepath)
                     (io/delete-file! tempfile)
                     (media/generate-thumbnail! filename)
                     (return file-item)))))

(defn- upload-files-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [request] :as env} {:keys [destination-id] :as mutation-props}]
  (let [content-size (file-uploader.helpers/request->content-size request)
        files-data   (file-uploader.helpers/request->files-data   request)]
       (if (capacity-handler.side-effects/capacity-limit-exceeded? content-size)
           (return :capacity-limit-exceeded)
           (when-let [destination-item (mongo-db/get-document-by-id "storage" destination-id)]
                     (let [destination-path (get  destination-item :media/path)
                           item-path        (conj destination-path {:media/id destination-id})]
                          ; A feltöltés véglegesítése előtt lefoglalja a szükséges tárhely-kapacitást,
                          ; így az egyszerre történő feltöltések nem léphetik át a megengedett tárhely-kapacitást ...
                          (core.side-effects/update-path-directories! env {:media/content-size content-size :media/path item-path} +)
                          (letfn [(f [result _ file-data]
                                     (let [file-data (assoc file-data :file-path item-path)]
                                          (conj result (upload-file-f env mutation-props file-data))))]
                                 (reduce-kv f [] files-data)))))))

(defmutation upload-files!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env mutation-props]
             {::pathom.co/op-name 'storage.file-uploader/upload-files!}
             (upload-files-f env mutation-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [upload-files!])

(pathom/reg-handlers! ::handlers HANDLERS)
