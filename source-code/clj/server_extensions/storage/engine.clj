
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
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (string) directory-id
  ; @param (namespaced map) media-item
  ;  {:media/id (string)}
  ;
  ; @return (namespaced map)
  [{:keys [request]} directory-id {:media/keys [id]}]
  (letfn [(prototype-f [document] (mongo-db/updated-document-prototype request :media document))
          (attach-f    [document] (update document :media/items vector/conj-item {:media/id id}))]
         (mongo-db/apply-document! "storage" directory-id attach-f {:prototype-f prototype-f})))

(defn detach-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (string) directory-id
  ; @param (namespaced map) media-item
  ;  {:media/id (string)}
  ;
  ; @return (namespaced map)
  [{:keys [request]} directory-id {:media/keys [id]}]
  (letfn [(prototype-f [document] (mongo-db/updated-document-prototype request :media document))
          (detach-f    [document] (update document :media/items vector/remove-item {:media/id id}))]
         (mongo-db/apply-document! "storage" directory-id detach-f {:prototype-f prototype-f})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-path-directories!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (namespaced map) media-item
  ;  {:media/filesize (B)
  ;   :media/path (namespaced maps in vector)}
  ; @param (function)(opt) operation
  ;  -, +
  ;
  ; @return (namespaced map)
  ([env media-item]
   (update-path-directories! env media-item nil))

  ([{:keys [request]} {:media/keys [filesize path]} operation]
   ; Mappák és fájlok létrehozásakor/feltöltésekor/törlésekor szükséges a tartalmazó (felmenő) mappák
   ; adatait aktualizálni:
   ; - Utolsó módosítás dátuma, és a felhasználó azonosítója {:media/modified-at ... :media/modified-by ...}
   ; - Tartalom mérete {:media/content-size ...}
   (letfn [(prototype-f [document] (mongo-db/updated-document-prototype request :media document))
           (update-f    [document] (if operation (update document :media/content-size operation filesize)
                                                 (return document)))
           (f [path] (when-let [{:media/keys [id]} (last path)]
                               (mongo-db/apply-document! "storage" id update-f {:prototype-f prototype-f})
                               (-> path vector/pop-last-item f)))]
          (f path))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [_ item-id]
  (mongo-db/get-document-by-id "storage" item-id))

(defn insert-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (namespaced map) media-item
  ;
  ; @return (namespaced map)
  [{:keys [request]} item]
  (mongo-db/insert-document! "storage" item {:prototype-f #(mongo-db/added-document-prototype request :media %)}))

(defn remove-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (namespaced map) media-item
  ;  {:media/id (string)}
  ;
  ; @return (string)
  [_ {:media/keys [id]}]
  (mongo-db/remove-document! "storage" id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filename
  [filename]
  (if-not (= filename SAMPLE-FILE-FILENAME)
          (media/delete-storage-file! filename))
  (media/delete-storage-thumbnail! filename))

(defn duplicate-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) source-filename
  ; @param (string) copy-filename
  [source-filename copy-filename]
  (media/duplicate-storage-file!      source-filename copy-filename)
  (media/duplicate-storage-thumbnail! source-filename copy-filename))
