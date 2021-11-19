
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.30
; Description:
; Version: v0.1.4
; Compatibility: x4.1.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.vector  :as vector]
              [local-db.api       :as local-db]
              [server-fruits.io   :as io]
              [x.mid-media.engine :as engine]
              [x.server-db.api    :as db]
              [x.server-user.api  :as user]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-media.engine
(def allowed-extensions                 engine/allowed-extensions)
(def filename->media-storage-uri        engine/filename->media-storage-uri)
(def filename->media-thumbnail-uri      engine/filename->media-thumbnail-uri)
(def filename->media-storage-filepath   engine/filename->media-storage-filepath)
(def filename->media-thumbnail-filepath engine/filename->media-thumbnail-filepath)
(def filename->temporary-filepath       engine/filename->temporary-filepath)
(def file-props->filepath               engine/file-props->filepath)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- source-directory-id->item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) source-directory-id
  ;
  ; @example
  ;  (source-directory-id->item-path "root")
  ;  => [{:directory/id "root"}]
  ;
  ; @return (maps in vector)
  [source-directory-id]
  (let [source-directory-document (local-db/get-document "directories" source-directory-id)
        source-directory-path     (get source-directory-document :path)
        source-directory-link     (db/document-id->document-link source-directory-id :directory)]
       (vector/conj-item source-directory-path source-directory-link)))

(defn filename->generated-filename
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filename
  ; @param (string) generated-file-id
  ;
  ; @example
  ;  (engine/filename->generated-filename "my-file.txt" "c59367ce-68fe-4d81-b439-4ce1b342afb7")
  ;  =>
  ;  "c59367ce-68fe-4d81-b439-4ce1b342afb7.txt"
  ;
  ; @example
  ;  (engine/filename->generated-filename "my-file" "c59367ce-68fe-4d81-b439-4ce1b342afb7")
  ;  =>
  ;  "c59367ce-68fe-4d81-b439-4ce1b342afb7"
  ;
  ; @return (string)
  [filename generated-file-id]
  (if-let [extension (io/filename->extension filename)]
          (str    generated-file-id "." extension)
          (return generated-file-id)))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-document-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) source-directory-id
  ; @param (keyword) file-id
  ; @param (map) file-document
  ;
  ; @return (map)
  ;  {:description (string)
  ;   :id (string)
  ;   :path (vector)
  ;   :permissions (map)
  ;   :tags (keywords in vector)}
  [{:keys [request]} [source-directory-id file-id file-document]]
  (let [file-path            (source-directory-id->item-path source-directory-id)]
       ;document-permissions (auth/request->document-permissions request)
       (merge {:description ""
               :tags        []}
              (param file-document)
              (user/request->modify-props request)
              (user/request->upload-props request)
              {:id          file-id
               :path        file-path})))
              ;:permissions document-permissions

(defn directory-document-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) source-directory-id
  ; @param (keyword) directory-id
  ; @param (map) directory-document
  ;
  ; @return (map)
  ;  {:content-size (B)
  ;   :created-at (string)
  ;   :created-by (map)
  ;   :description (string)
  ;   :id (string)
  ;   :items (maps in vector)
  ;   :modified-at (string)
  ;   :modified-by (map)
  ;   :tags (keywords in vector)}
  [{:keys [request]} [source-directory-id directory-id directory-document]]
  (let [directory-path       (source-directory-id->item-path source-directory-id)]
       ;document-permissions (auth/request->document-permissions request)
       (merge {:description ""
               :tags        []}
              (param directory-document)
              (user/request->create-props request)
              (user/request->modify-props request)
              {:content-size 0
               :id           directory-id
               :items        []
               :path         directory-path})))
              ;:permissions  document-permissions

(defn updated-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) directory-id
  ; @param (map) updated-props
  ;
  ; @example
  ;  (a/prot {:request {:session {:user-account/id "my-user"}}}
  ;          [:my-directory {:my-key "My value"}]
  ;          updated-props-prototype)
  ;  => {:my-key "My value" :modified-at "..." :modified-by {:user-account/id "my-user"}}
  ;
  ; @return (map)
  [{:keys [request]} [_ updated-props]]
  (merge (param updated-props)
         (user/request->modify-props request)))



;; -- Attach/detach items -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn detach-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) action-props
  ;  {:source-directory-id (string)
  ;   :selected-item (map)
  ;    {:directory/id (string)(opt)
  ;     :file/id (string)(opt)}}
  ;
  ; @usage
  ;  (detach-item! {...} {:source-directory-id "root" :selected-item {:directory/id "my-directory"}})
  ;
  ; @usage
  ;  (detach-item! {...} {:source-directory-id "root" :selected-item {:file/id "my-file"}})
  ;
  ; @return (string)
  [{:keys [request]} {:keys [source-directory-id selected-item]}]
  (local-db/update-document! "directories" source-directory-id
                                           ; Remove file link from :items vector
                             (fn [%] (-> % (update :items vector/remove-item selected-item)
                                           ; Update modify data in source-directory document
                                           (merge (user/request->modify-props request)))))
  (return "Item detached"))

(defn attach-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) action-props
  ;  {:destination-directory-id (string)
  ;   :selected-item (map)
  ;    {:directory/id (string)(opt)
  ;     :file/id (string)(opt)}}
  ;
  ; @usage
  ;  (attach-item! {...} {:destination-directory-id "root" :selected-item {:directory/id "my-directory"}})
  ;
  ; @usage
  ;  (attach-item! {...} {:destination-directory-id "root" :selected-item {:file/id "my-file"}})
  ;
  ; @return (string)
  [{:keys [request]} {:keys [destination-directory-id selected-item]}]
  (local-db/update-document! "directories" destination-directory-id
                                           ; Add file link from :items vector
                             (fn [%] (-> % (update :items vector/conj-item selected-item)
                                           ; Update modify data in source-directory document
                                           (merge (user/request->modify-props request)))))
  (return "Item attached"))



;; -- Space calculator functions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-directory-content-size!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A content-size paraméterként átadott értékkel az f paraméterként átadott
  ; függvénnyel módosítja a directory-id paraméterként átadott azonosítójú
  ; mappa {:content-size ...} tulajdonságát.
  ;
  ; @param (string) directory-id
  ; @param (B) content-size
  ; @param (function) f
  ;
  ; @usage
  ;  (update-directory-content-size! "my-directory" 15324 +)
  ;
  ; @usage
  ;  (update-directory-content-size! "my-directory" 15324 -)
  ;
  ; @return (string)
  [directory-id content-size f]
  (local-db/update-document! "directories" directory-id
                             (fn [%] (update % :content-size f content-size)))

  (return "Directory content-size updated"))

(defn update-path-content-size!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A content-size paraméterként átadott értékkel az f paraméterként átadott
  ; függvénnyel módosítja a directory-id paraméterként átadott azonosítójú
  ; mappa és annak összes felmenőjének {:content-size ...} tulajdonságát.
  ;
  ; @param (string) directory-id
  ; @param (B) content-size
  ; @param (function) f
  ;
  ; @usage
  ;  (update-path-content-size! "my-directory" 15324 +)
  ;
  ; @usage
  ;  (update-path-content-size! "my-directory" 15324 -)
  ;
  ; @return (string)
  [directory-id content-size f]
  (let [path           (local-db/get-document-item "directories" directory-id :path)
        directory-link (db/document-id->document-link directory-id :directory)]
       (reduce (fn [_ directory-link]
                   (let [directory-id (db/document-link->document-id directory-link)]
                        (update-directory-content-size! directory-id content-size f)))
               (param nil)
               (vector/conj-item path directory-link)))

  (return "Path content-size updated"))
