
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.storage.file-uploader.mutations
    (:require [mid-fruits.candy   :refer [param return]]
              [mongo-db.api       :as mongo-db]
              [pathom.api         :as pathom]
              [server-fruits.io   :as io]
              [x.server-media.api :as media]
              [com.wsscode.pathom3.connect.operation                   :as pathom.co :refer [defmutation]]
              [server-extensions.storage.capacity-handler.side-effects :as capacity-handler.side-effects]
              [server-extensions.storage.engine                        :as engine]
              [server-extensions.storage.side-effects                  :as side-effects]))



;; -- Helpers -----------------------------------------------------------------
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
  [{:keys [params]}]
  (letfn [(f [files-data dex file-data] (assoc files-data dex (update file-data :tempfile str)))]
         (reduce-kv f {} (dissoc params :query))))

(defn request->content-size
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [params]}]
  (letfn [(f [content-size _ {:keys [size]}] (+ content-size size))]
         (reduce-kv f 0 (dissoc params :query))))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-item-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [file-path filename size]}]
  ; - A fájlokkal ellentétben a mappák {:media/mime-type "..."} tulajdonsága nem állapítható meg a nevükből
  ; - A fájlok {:media/mime-type "..."} tulajdonsága is eltárolásra kerül, hogy a mappákhoz hasonlóan
  ;   a fájlok is rendelkezzenek {:media/mime-type "..."} tulajdonsággal
  (let [file-id            (mongo-db/generate-id)
        generated-filename (engine/file-id->filename file-id filename)
        mime-type          (io/filename->mime-type   filename)]
       {:media/alias     filename
        :media/filename  generated-filename
        :media/filesize  size
        :media/id        file-id
        :media/mime-type mime-type
        :media/path      file-path
        :media/description ""}))



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
  (let [file-item (file-item-prototype file-data)
        filename  (get file-item :media/filename)
        filepath  (media/filename->media-storage-filepath filename)]
       (if (side-effects/attach-item! env destination-id file-item)
           (when-let [file-item (side-effects/insert-item! env file-item)]
                     ; Copy the temporary file to storage, and delete the temporary file
                     (io/copy-file!   tempfile filepath)
                     (io/delete-file! tempfile)
                     (media/generate-thumbnail! filename)
                     (return file-item)))))

(defn- upload-files-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [request] :as env} {:keys [destination-id] :as mutation-props}]
  (let [content-size (request->content-size request)
        files-data   (request->files-data   request)]
       (if (capacity-handler.side-effects/capacity-limit-exceeded? content-size)
           (return :capacity-limit-exceeded)
           (when-let [destination-item (mongo-db/get-document-by-id "storage" destination-id)]
                     (let [destination-path (get  destination-item :media/path)
                           item-path        (conj destination-path {:media/id destination-id})]
                          ; A feltöltés véglegesítése előtt lefoglalja a szükséges tárhely-kapacitást,
                          ; így az egyszerre történő feltöltések nem léphetik át a megengedett tárhely-kapacitást ...
                          (side-effects/update-path-directories! env {:media/content-size content-size :media/path item-path} +)
                          (letfn [(f [result _ file-data]
                                     (let [file-data (assoc file-data :file-path item-path)]
                                          (conj result (upload-file-f env mutation-props file-data))))]
                                 (reduce-kv f [] files-data)))))))

(defmutation upload-files!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env mutation-props]
             {::pathom.co/op-name 'storage.file-uploader/upload-files!}
             (upload-files-f env mutation-props))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [upload-files!])

(pathom/reg-handlers! ::handlers HANDLERS)
