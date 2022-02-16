
(ns server-extensions.storage.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.vector  :as vector]
              [mongo-db.api       :as mongo-db]
              [server-fruits.io   :as io]
              [x.server-media.api :as media]
              [mid-extensions.storage.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-extensions.storage.engine
(def ROOT-DIRECTORY-ID    engine/ROOT-DIRECTORY-ID)
(def SAMPLE-FILE-ID       engine/SAMPLE-FILE-ID)
(def SAMPLE-FILE-FILENAME engine/SAMPLE-FILE-FILENAME)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-id->filename
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) file-id
  ; @param (string) filename
  ;
  ; @example
  ;  (engine/file-id->filename "my-item" "my-image.png")
  ;  =>
  ;  "my-item.png"
  ;
  ; @return (string)
  [file-id filename]
  (if-let [extension (io/filename->extension filename)]
          (str    file-id "." extension)
          (return file-id)))



;; -- Attach/detach item functions --------------------------------------------
;; ----------------------------------------------------------------------------

(defn attach-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ directory-id {:media/keys [id] :as media-item}]
  (letfn [(attach-f [document] (update document :media/items vector/conj-item {:media/id id}))]
         (mongo-db/apply-document! "storage" directory-id attach-f)))

(defn detach-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ directory-id {:media/keys [id] :as media-item}]
  (letfn [(detach-f [document] (update document :media/items vector/remove-item {:media/id id}))]
         (mongo-db/apply-document! "storage" directory-id detach-f)))

(defn item-attached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ directory-id {:media/keys [id] :as media-item}]
  (boolean (if-let [{:media/keys [items]} (mongo-db/get-document-by-id "storage" directory-id)]
                   (vector/contains-item? items {:media/id id}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-path-directories!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (namespaced map) media-item
  ;  {:media/content-size (B)
  ;   :media/filesize (B)
  ;   :media/path (namespaced maps in vector)}
  ; @param (function)(opt) operation
  ;  -, +
  ;
  ; @return (namespaced map)
  ([env media-item]
   (update-path-directories! env media-item nil))

  ([{:keys [request]} {:media/keys [content-size filesize path]} operation]
   ; Mappák és fájlok létrehozásakor/feltöltésekor/törlésekor/duplikálásakor szükséges
   ;  a tartalmazó (felmenő) mappák adatait aktualizálni:
   ; - Utolsó módosítás dátuma, és a felhasználó azonosítója {:media/modified-at ... :media/modified-by ...}
   ; - Tartalom méretének {:media/content-size ...} aktualizálása
   (letfn [(prototype-f [document] (mongo-db/updated-document-prototype request :media document))
           (update-f    [document] (cond-> document content-size (update :media/content-size operation content-size)
                                                    filesize     (update :media/content-size operation filesize)))
           (f [path] (when-let [{:media/keys [id]} (last path)]
                               (if operation (mongo-db/apply-document! "storage" id update-f {:prototype-f prototype-f})
                                             (mongo-db/apply-document! "storage" id return   {:prototype-f prototype-f}))
                               (-> path vector/pop-last-item f)))]
          (f path))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env item-id]
  (mongo-db/get-document-by-id "storage" item-id))

(defn insert-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [request] :as env} item]
  (mongo-db/insert-document! "storage" item {:prototype-f #(mongo-db/added-document-prototype request :media %)}))

(defn remove-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env {:media/keys [id] :as media-item}]
  (mongo-db/remove-document! "storage" id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [filename]
  (if-not (= filename SAMPLE-FILE-FILENAME)
          (media/delete-storage-file! filename))
  (media/delete-storage-thumbnail! filename))

(defn duplicate-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [source-filename copy-filename]
  (media/duplicate-storage-file!      source-filename copy-filename)
  (media/duplicate-storage-thumbnail! source-filename copy-filename))
