
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.21
; Description:
; Version: v0.9.8
; Compatibility: x4.3.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.item-handler
    (:require [local-db.api          :as local-db]
              [mid-fruits.candy      :refer [param return]]
              [mid-fruits.keyword    :as keyword]
              [mid-fruits.gestures   :as gestures]
              [mid-fruits.random     :as random]
              [mid-fruits.vector     :as vector]
              [server-fruits.io      :as io]
              [x.server-db.api       :as db]
              [x.server-media.engine :as engine]
              [x.server-core.api     :as a]
              [x.server-sync.api     :as sync]
              [com.wsscode.pathom3.connect.operation :as pathom.co]
              [x.server-media.thumbnail-handler      :as thumbnail-handler]))

             ; TODO ...
             ;[project-emulator.auth.api :as auth]



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name source-directory
;  Parent directory
;
; @name destination-directory
;  Parent directory



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def ROOT-DIRECTORY-ID "home")



;; -- Converters --------------------------------------------------------------
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

(defn- filename->generated-filename
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filename
  ; @param (string) generated-file-id
  ;
  ; @example
  ;  (filename->generated-filename "my-file.txt" "c59367ce-68fe-4d81-b439-4ce1b342afb7")
  ;  => "c59367ce-68fe-4d81-b439-4ce1b342afb7.txt"
  ;
  ; @example
  ;  (filename->generated-filename "my-file" "c59367ce-68fe-4d81-b439-4ce1b342afb7")
  ;  => "c59367ce-68fe-4d81-b439-4ce1b342afb7"
  ;
  ; @return (string)
  [filename generated-file-id]
  (if-let [extension (io/filename->extension filename)]
          (str    generated-file-id "." extension)
          (return generated-file-id)))

(defn- file-alias->copy-file-alias
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) file-alias
  ; @param (strings in vector) concurent-aliases
  ; @param (string) suffix
  ;
  ; @example
  ;  (file-alias->copy-file-alias "My file.txt" [] "copy")
  ;  => "My file copy.txt"
  ;
  ; @example
  ;  (file-alias->copy-file-alias "My file" [] "copy")
  ;  => "My file copy"
  ;
  ; @return (string)
  [file-alias concurent-aliases suffix]
  (if-let [extension (io/filename->extension file-alias)]
          (let [basename      (io/filename->basename file-alias)
                copy-basename (gestures/item-label->duplicated-item-label basename concurent-aliases suffix)]
               (str copy-basename "." extension))
          (gestures/item-label->duplicated-item-label file-alias concurent-aliases suffix)))

(defn- directory-alias->copy-directory-alias
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) directory-alias
  ; @param (strings in vector) concurent-aliases
  ; @param (string) suffix
  ;
  ; @example
  ;  (directory-alias->copy-directory-alias "My directory" [] "copy")
  ;  => "My directory copy"
  ;
  ; @return (string)
  [directory-alias concurent-aliases suffix]
  (gestures/item-label->duplicated-item-label directory-alias concurent-aliases suffix))



;; -- Attach/detach items -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- detach-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
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
  [env {:keys [source-directory-id selected-item]}]
  (local-db/update-document! "directories" source-directory-id
                                           ; Remove file link from :items vector
                             (fn [%] (-> % (update :items vector/remove-item selected-item)
                                           ; Update modify data in source-directory document
                                           (merge (sync/env->modify-props env)))))
  (return "Item detached"))

(defn- attach-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
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
  [env {:keys [destination-directory-id selected-item]}]
  (local-db/update-document! "directories" destination-directory-id
                                           ; Add file link from :items vector
                             (fn [%] (-> % (update :items vector/conj-item selected-item)
                                           ; Update modify data in source-directory document
                                           (merge (sync/env->modify-props env)))))
  (return "Item attached"))



;; -- Space calculator functions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn- update-directory-content-size!
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

(defn- update-path-content-size!
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



;; -- Directory functions -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-directory-items-alias-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) directory-id
  ;
  ; @example
  ;  (get-directory-items-alias-list "my-directory")
  ;  => {:directory ["My directory" "Your directory"]
  ;      :file ["My file" "Your file"]}
  ;
  ; @return (map)
  [directory-id]
  (let [directory-items (local-db/get-document-item "directories" directory-id :items)]
       (reduce (fn [result directory-item]
                   (let [directory-item-type (db/document-link->namespace   directory-item)
                         directory-item-id   (db/document-link->document-id directory-item)]
                        (cond (db/document-link->namespace? directory-item :directory)
                              (let [alias (local-db/get-document-item "directories" directory-item-id :alias)]
                                   (update result :directory vector/conj-item alias))
                              (db/document-link->namespace? directory-item :file)
                              (let [alias (local-db/get-document-item "files" directory-item-id :alias)]
                                   (update result :file vector/conj-item alias)))))
               (param {})
               (param directory-items))))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-document-prototype
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
  [env [source-directory-id file-id file-document]]
  (let [request              (sync/env->request env)
       ;document-permissions (auth/request->document-permissions request)
        file-path            (source-directory-id->item-path source-directory-id)]
       (merge {:description ""
               :tags        []}
              (param file-document)
              (sync/env->modify-props env)
              (sync/env->upload-props env)
              {:id          file-id
               :path        file-path})))
              ;:permissions document-permissions

(defn- directory-document-prototype
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
  [env [source-directory-id directory-id directory-document]]
  (let [request              (sync/env->request env)
       ;document-permissions (auth/request->document-permissions request)
        directory-path       (source-directory-id->item-path source-directory-id)]
       (merge {:description ""
               :tags        []}
              (param directory-document)
              (sync/env->create-props env)
              (sync/env->modify-props env)
              {:content-size 0
               :id           directory-id
               :items        []
               :path         directory-path})))
              ;:permissions  document-permissions

(defn- updated-props-prototype
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
  [env [_ updated-props]]
  (merge (param updated-props)
         (sync/env->modify-props env)))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(pathom.co/defresolver get-directory-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;  {:directory/id (string)}
  ;
  ; @return (map)
  [env {:keys [directory/id]}]
  {::pathom.co/output [:directory/alias
                       :directory/content-size
                       :directory/created-at
                       :directory/created-by
                       :directory/id
                       :directory/items
                       :directory/items-count
                       :directory/modified-at
                       :directory/modified-by]}

  (let [directory-document    (local-db/get-document "directories" id)
        _ (println (str "directory-id: " id))
        _ (println (str "directory-document: " directory-document))
        directory-items       (get directory-document :items)
        directory-items-count (count directory-items)

        directory-document    (assoc directory-document :items-count directory-items-count)]
        
       (println (str "result: " (db/document->namespaced-document directory-document :directory)))
       (db/document->namespaced-document directory-document :directory)))

(pathom.co/defresolver get-file-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;  {:file/id (string)}
  ;
  ; @return (map)
  [env {:keys [file/id]}]
  {::pathom.co/output [:file/alias
                       :file/id
                       :file/filename
                       :file/filesize
                       :file/modified-at
                       :file/modified-by
                       :file/uploaded-at
                       :file/uploaded-by]}

  (let [file-document (local-db/get-document "files" id)]
       (db/document->namespaced-document file-document :file)))



;; -- Update functions --------------------------------------------------------
;; ----------------------------------------------------------------------------

(pathom.co/defmutation update-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:directory-id (string)
  ;   :updated-props (map)}
  ;
  ; @return (string)
  [env {:keys [directory-id updated-props]}]
  {::pathom.co/op-name 'media/update-directory!}

  (let [updated-props (a/sub-prot env [directory-id updated-props] updated-props-prototype)]
       (local-db/update-document! "directories" directory-id merge updated-props)
       (return "Directory updated")))

(pathom.co/defmutation update-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:file-id (string)
  ;   :updated-props (map)}
  ;
  ; @return (string)
  [env {:keys [file-id updated-props]}]
  {::pathom.co/op-name 'media/update-file!}

  (let [updated-props (a/sub-prot env [file-id updated-props] updated-props-prototype)]
       (local-db/update-document! "files" file-id merge updated-props)
       (return "File updated")))



;; -- Create functions --------------------------------------------------------
;; ----------------------------------------------------------------------------

(pathom.co/defmutation create-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:destination-directory-id (string)
  ;   :directory-alias (string)}
  ;
  ; @return (string)
  [env {:keys [destination-directory-id directory-alias]}]
  {::pathom.co/op-name 'media/create-directory!}

  (let [directory-id       (random/generate-string)
        directory-link     (db/document-id->document-link directory-id :directory)
        directory-document {:alias directory-alias}
        directory-document (a/sub-prot env [destination-directory-id directory-id directory-document]
                                       directory-document-prototype)]

       ; Add the new directory document to the "directories" collection
       (local-db/add-document! "directories" directory-document)

       ; Add the new directory link to source-directory document
       (attach-item! env {:destination-directory-id destination-directory-id
                          :selected-item            directory-link})

       (return "Directory created")))



;; -- Delete functions --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- delete-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) action-props
  ;  {:source-directory-id (string)
  ;   :selected-item (map)
  ;    {:file/id (string)}}
  ;
  ; @return (string)
  [env {:keys [source-directory-id selected-item] :as action-props}]
  (let [file-id       (db/document-link->document-id selected-item)
        file-document (local-db/get-document "files" file-id)
        filename      (get file-document :filename)
        filesize      (get file-document :filesize)
        filepath      (engine/filename->media-storage-filepath filename)]

    ; Delete the file physically
    (io/delete-file! filepath)

    ; Delete file document from "files" collection
    (local-db/remove-document! "files" file-id)

    ; Remove file link from the source-directory document
    (detach-item! env action-props)

    ; Update the ancestor directories content-size
    (update-path-content-size! source-directory-id filesize -)

    (return "File deleted")))

(defn- delete-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) action-props
  ;  {:source-directory-id (string)
  ;   :selected-item (map)
  ;    {:directory/id (string)}}
  ;
  ; @return (string)
  [env {:keys [source-directory-id selected-item] :as action-props}]
  (let [directory-id    (db/document-link->document-id selected-item)
        directory-items (local-db/get-document-item "directories" directory-id :items)]

       ; Remove directory link from source-directory document
       (detach-item! env action-props)

       ; Iterates through items in directory
       (doseq [selected-item directory-items]
              (let [selected-item-type (db/document-link->namespace selected-item)
                    action-props       (assoc action-props :selected-item selected-item)]
                   (case selected-item-type
                         :file      (delete-file!      env action-props)
                         :directory (delete-directory! env action-props)
                         :non-existing-type)))

       ; Delete directory document from "directories" collection
       (local-db/remove-document! "directories" directory-id)

       (return "Directory deleted")))

(pathom.co/defmutation delete-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:source-directory-id (string)
  ;   :selected-items (maps in vector)}
  ;
  ; @usage
  ;  (delete-items! {...}
  ;                 {:source-directory-id "my-directory"
  ;                  :selected-items [{:directory/id "my-subdirectory"} {:file/id "my-file"}]}
  ;
  ; @return (string)
  [env {:keys [source-directory-id selected-items] :as mutation-props}]
  {::pathom.co/op-name 'media/delete-items!}

  ; If it is directory, or contains a directory it should be recursive!
  (doseq [selected-item selected-items]
         (let [selected-item-type (db/document-link->namespace selected-item)
               action-props       (assoc mutation-props :selected-item selected-item)]
              (case selected-item-type
                    :file       (delete-file!      env action-props)
                    :directory  (delete-directory! env action-props)
                    :non-existing-type)))

  (return "Items deleted"))



;; -- Copy functions ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- copy-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) action-props
  ;  {:copy-item-suffix (string)
  ;   :destination-directory-id (string)
  ;   :selected-item (map)
  ;    {:file/id (string)}}
  ;
  ; @return (string)
  [env {:keys [copy-item-suffix destination-directory-id directory-files-alias-list selected-item]}]

        ; Source file details
  (let [file-id        (db/document-link->document-id selected-item)
        file-document  (local-db/get-document "files" file-id)
        file-alias     (get file-document :alias)
        filename       (get file-document :filename)
        filesize       (get file-document :filesize)
        filepath       (engine/filename->media-storage-filepath filename)

        ; Copy file details
        copy-file-id       (random/generate-string)
        copy-filename      (filename->generated-filename filename copy-file-id)
        copy-file-alias    (file-alias->copy-file-alias  file-alias directory-files-alias-list copy-item-suffix)
        copy-filepath      (engine/filename->media-storage-filepath copy-filename)
        copy-file-document (a/sub-prot env [destination-directory-id copy-file-id file-document]
                                       file-document-prototype)
        copy-file-document (merge copy-file-document {:alias    copy-file-alias
                                                      :filename copy-filename})
        copy-file-link     (db/document-id->document-link copy-file-id :file)]

    ; Copy the file physically
    (io/copy-file! filepath copy-filepath)

    ; Add the new file document to the "files" collection
    (local-db/add-document! "files" copy-file-document)

    ; Update the ancestor directories content-size
    (update-path-content-size! destination-directory-id filesize +)

    ; Update file document modify data
    (let [modify-props (sync/env->modify-props env)]
         (local-db/update-document! "files" file-id merge modify-props))

    ; Add the new file link to destination-directory document
    (attach-item! env {:destination-directory-id destination-directory-id
                       :selected-item            copy-file-link})))

(defn- copy-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) action-props
  ;  {:copy-item-suffix (string)
  ;   :destination-directory-id (string)
  ;   :selected-item (map)
  ;    {:directory/id (string)}}
  ;
  ; @return (string)
  [env {:keys [copy-item-suffix destination-directory-id directory-subdirectories-alias-list selected-item] :as action-props}]
  (let [directory-id            (db/document-link->document-id selected-item)
        directory-document      (local-db/get-document "directories" directory-id)
        directory-alias         (get directory-document :alias)
        copy-directory-id       (random/generate-string)
        copy-directory-alias    (directory-alias->copy-directory-alias directory-alias directory-subdirectories-alias-list copy-item-suffix)
        copy-directory-document (a/sub-prot env [destination-directory-id copy-directory-id directory-document]
                                            directory-document-prototype)
        copy-directory-document (merge copy-directory-document {:alias copy-directory-alias})
        copy-directory-link     (db/document-id->document-link copy-directory-id :directory)]

    ; Add the new directory document to the "directories" collection
    (local-db/add-document! "directories" copy-directory-document)

    ; Add the new directory link to the destination-directory document
    (attach-item! env {:destination-directory-id destination-directory-id
                       :selected-item            copy-directory-link})

    ; Update directory document modify data
    (let [modify-props (sync/env->modify-props env)]
         (local-db/update-document! "directories" destination-directory-id merge modify-props))

    ; Iterates through items in directory
    (let [directory-items                     (get directory-document :items)
          directory-items-alias-list          (get-directory-items-alias-list directory-id)
          directory-files-alias-list          (get directory-items-alias-list :file)
          directory-subdirectories-alias-list (get directory-items-alias-list :directory)]
         (doseq [selected-item directory-items]
                (let [selected-item-type (db/document-link->namespace selected-item)
                      action-props       {:copy-item-suffix                    copy-item-suffix
                                          :destination-directory-id            copy-directory-id
                                          :directory-files-alias-list          directory-files-alias-list
                                          :directory-subdirectories-alias-list directory-subdirectories-alias-list
                                          :selected-item                       selected-item}]
                     (case selected-item-type
                           :file       (copy-file!      env action-props)
                           :directory  (copy-directory! env action-props)
                           :non-existing-type))))))

(pathom.co/defmutation copy-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:copy-item-suffix (string)
  ;   :destination-directory-id (string)
  ;   :selected-items (maps in vector)}
  ;
  ; @usage
  ;  (copy-items! {...}
  ;               {:copy-item-suffix "copy"
  ;                :destination-directory-id "my-directory"
  ;                :selected-items [{:directory/id "my-subdirectory"} {:file/id "my-file"}]}
  ;
  ; @return (string)
  [env {:keys [copy-item-suffix destination-directory-id selected-items] :as mutation-props}]
  {::pathom.co/op-name 'media/copy-items!}

  ; Iterates through selected items
  (let [directory-items-alias-list          (get-directory-items-alias-list destination-directory-id)
        directory-files-alias-list          (get directory-items-alias-list :file)
        directory-subdirectories-alias-list (get directory-items-alias-list :directory)]
       (doseq [selected-item selected-items]
              (let [selected-item-type (db/document-link->namespace selected-item)
                    action-props       {:copy-item-suffix                    copy-item-suffix
                                        :destination-directory-id            destination-directory-id
                                        :directory-files-alias-list          directory-files-alias-list
                                        :directory-subdirectories-alias-list directory-subdirectories-alias-list
                                        :selected-item                       selected-item}]
                   (case selected-item-type
                         :file       (copy-file!      env action-props)
                         :directory  (copy-directory! env action-props)
                         :non-existing-type))))

  (return "Items copied"))



;; -- Move functions ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- move-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:destination-directory-id (string)
  ;   :selected-item (map)
  ;   :source-directory-id (string)}
  ;
  ; @return (string)
  [env {:keys [destination-directory-id selected-item source-directory-id]}]
  (let [file-id  (db/document-link->document-id selected-item)
        filesize (local-db/get-document-item "files" file-id :filesize)]

       ; Remove item link from the source-directory document
       (detach-item! env {:source-directory-id source-directory-id
                          :selected-item       selected-item})

       ; Update the ancestor directories content-size
       (update-path-content-size! source-directory-id      filesize -)
       (update-path-content-size! destination-directory-id filesize +)

       ; Add item link to the destination-directory document
       (attach-item! env {:destination-directory-id destination-directory-id
                          :selected-item            selected-item})

       ; Update file document modify data
       (let [modify-props (sync/env->modify-props env)]
            (local-db/update-document! "files" file-id merge modify-props))

       (return "File moved")))

(defn- move-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:destination-directory-id (string)
  ;   :selected-item (map)
  ;   :source-directory-id (string)}
  ;
  ; @return (string)
  [env {:keys [destination-directory-id selected-item source-directory-id]}]

  ; Remove item link from the source-directory document
  (detach-item! env {:source-directory-id source-directory-id
                     :selected-item       selected-item})

  ; Add item link to the destination-directory document
  (attach-item! env {:destination-directory-id destination-directory-id
                     :selected-item            selected-item})

  ; Update directory documents modify data
  (let [modify-props (sync/env->modify-props env)]
       (local-db/update-document! "directories" destination-directory-id merge modify-props)
       (local-db/update-document! "directories" source-directory-id      merge modify-props))

  (return "Directory moved"))

(pathom.co/defmutation move-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:destination-directory-id (string)
  ;   :selected-items (maps in vector)
  ;   :source-directory-id (string)}
  ;
  ; @usage
  ;  (move-items! {...}
  ;               {:destination-directory-id "my-directory"
  ;                :selected-items [{:directory/id "my-subdirectory"} {:file/id "my-file"}]
  ;                :source-directory-id "your-directory"}
  ;
  ; @return (string)
  [env {:keys [destination-directory-id selected-items source-directory-id] :as mutation-props}]
  {::pathom.co/op-name 'media/move-items!}

  (doseq [selected-item selected-items]
         (let [selected-item-type (db/document-link->namespace selected-item)
               action-props       (assoc mutation-props :selected-item selected-item)]
              (case selected-item-type
                    :file      (move-file!      env action-props)
                    :directory (move-directory! env action-props)
                    :non-existing-type)))

  (return "Items moved"))



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
        generated-filename   (filename->generated-filename filename file-id)
        destination-filepath (engine/filename->media-storage-filepath generated-filename)
        file-document {:alias filename :filename generated-filename :filesize filesize}
        file-document (a/sub-prot env [destination-directory-id file-id file-document]
                                  file-document-prototype)
        file-link     (db/document-id->document-link file-id :file)]

       ; Copy the temporary file to storage
       (io/copy-file! temp-filepath destination-filepath)

       ; Add file link to the destination-directory document
       (attach-item! env {:destination-directory-id destination-directory-id
                          :selected-item            file-link})

       ; Add the file document to the "files" collection
       (local-db/add-document! "files" file-document)

       ; Update the ancestor directories content-size
       (update-path-content-size! destination-directory-id filesize +)

       ; Delete the temporary file
       (io/delete-file! temp-filepath)

       ; Generate file thumbnail
       ;(thumbnail-handler/generate-thumbnail! file-id)

       (return "File uploaded")))

(pathom.co/defmutation upload-files!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:destination-directory-id (string)
  ;   :processed-files-data (map)
  ;    {"0" (map)
  ;     {:content-type (string)
  ;      :filename (string)
  ;      :size (B)
  ;      :tempfile (string)}
  ;     "1" (map)
  ;     "2" (map)
  ;     ...}}
  ;
  ; @return (string)
  [env {:keys [destination-directory-id processed-files-data]}]
  {::pathom.co/op-name 'media/upload-files!}

  (doseq [[_ {:keys [filename size tempfile] :as file}] processed-files-data]
         (let [action-props {:destination-directory-id destination-directory-id
                             :filename                 filename
                             :filesize                 size
                             :temp-filepath            tempfile}]
              (upload-file! env action-props)))

  (return "Files uploaded"))
