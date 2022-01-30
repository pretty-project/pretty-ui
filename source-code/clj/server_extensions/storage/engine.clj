
(ns server-extensions.storage.engine
    (:require [mid-fruits.vector :as vector]
              [mongo-db.api      :as mongo-db]
              [mid-extensions.storage.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-extensions.storage.engine
(def ROOT-DIRECTORY-ID    engine/ROOT-DIRECTORY-ID)
(def SAMPLE-FILE-ID       engine/SAMPLE-FILE-ID)
(def SAMPLE-FILE-FILENAME engine/SAMPLE-FILE-FILENAME)



;; -- Attach/detach item functions --------------------------------------------
;; ----------------------------------------------------------------------------

(defn attach-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (string) directory-id
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [{:keys [request]} directory-id item-id]
  (letfn [(prototype-f [document] (mongo-db/updated-document-prototype request :media document))
          (attach-f    [document] (update document :media/items vector/conj-item {:media/id item-id}))]
         (mongo-db/apply-document! "storage" directory-id attach-f {:prototype-f prototype-f})))

(defn detach-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (string) directory-id
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [{:keys [request]} directory-id item-id]
  (letfn [(prototype-f [document] (mongo-db/updated-document-prototype request :media document))
          (detach-f    [document] (update document :media/items vector/remove-item {:media/id item-id}))]
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
           (update-f    [document] (if operation (update document :media/content-size operation filesize)))
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
  ; @param (namespaced map) item
  ;
  ; @return (namespaced map)
  [{:keys [request]} item]
  (mongo-db/insert-document! "storage" item {:prototype-f #(mongo-db/added-document-prototype request :media %)}))

(defn remove-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (string) item-id
  ;
  ; @return (string)
  [_ item-id]
  (mongo-db/remove-document! "storage" item-id))
