
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
              [pathom.api            :as pathom]
              [server-fruits.io      :as io]
              [x.server-db.api       :as db]
              [x.server-media.engine :as engine]
              [x.server-core.api     :as a]
              [x.server-user.api     :as user]
              [com.wsscode.pathom3.connect.operation :as pathom.co]))




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

(defn- file-alias->copy-file-alias
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) file-alias
  ; @param (strings in vector) concurent-aliases
  ; @param (string) suffix
  ;
  ; @example
  ;  (file-alias->copy-file-alias "My file.txt" [] "copy")
  ;  => "My file copy.txt"
  ;
  ; @example
  ;  (file-alias->copy-file-alias "My file" [] "copy")
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
  ;  (directory-alias->copy-directory-alias "My directory" [] "copy")
  ;  => "My directory copy"
  ;
  ; @return (string)
  [directory-alias concurent-aliases suffix]
  (gestures/item-label->duplicated-item-label directory-alias concurent-aliases suffix))



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
                       :directory/item-count
                       :directory/modified-at
                       :directory/modified-by]}

  (let [directory-document   (local-db/get-document "directories" id)
        directory-items      (get directory-document :items)
        directory-item-count (count directory-items)
        directory-document   (assoc directory-document :item-count directory-item-count)]
       (db/document->namespaced-document directory-document :directory)))

(pathom/reg-handler! :get-directory-data get-directory-data)

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

(pathom/reg-handler! :get-file-data get-file-data)



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

  (let [updated-props (a/sub-prot env [directory-id updated-props] engine/updated-props-prototype)]
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

  (let [updated-props (a/sub-prot env [file-id updated-props] engine/updated-props-prototype)]
       (local-db/update-document! "files" file-id merge updated-props)
       (return "File updated")))

(pathom/reg-handler! :update-file update-file!)



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
                                       engine/directory-document-prototype)]

       ; Add the new directory document to the "directories" collection
       (local-db/add-document! "directories" directory-document)

       ; Add the new directory link to source-directory document
       (engine/attach-item! env {:destination-directory-id destination-directory-id
                                 :selected-item            directory-link})

       (return "Directory created")))

(pathom/reg-handler! :create-directory! create-directory!)



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
    (engine/detach-item! env action-props)

    ; Update the ancestor directories content-size
    (engine/update-path-content-size! source-directory-id filesize -)

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
       (engine/detach-item! env action-props)

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

(pathom/reg-handler! :delete-items! delete-items!)



;; -- Copy functions ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- copy-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) action-props
  ;  {:copy-item-suffix (string)
  ;   :destination-directory-id (string)
  ;   :selected-item (map)
  ;    {:file/id (string)}}
  ;
  ; @return (string)
  [{:keys [request] :as env}
   {:keys [copy-item-suffix destination-directory-id directory-files-alias-list selected-item]}]

        ; Source file details
  (let [file-id        (db/document-link->document-id selected-item)
        file-document  (local-db/get-document "files" file-id)
        file-alias     (get file-document :alias)
        filename       (get file-document :filename)
        filesize       (get file-document :filesize)
        filepath       (engine/filename->media-storage-filepath filename)

        ; Copy file details
        copy-file-id       (random/generate-string)
        copy-filename      (engine/filename->generated-filename filename copy-file-id)
        copy-file-alias    (file-alias->copy-file-alias file-alias directory-files-alias-list copy-item-suffix)
        copy-filepath      (engine/filename->media-storage-filepath copy-filename)
        copy-file-document (a/sub-prot env [destination-directory-id copy-file-id file-document]
                                       engine/file-document-prototype)
        copy-file-document (merge copy-file-document {:alias    copy-file-alias
                                                      :filename copy-filename})
        copy-file-link     (db/document-id->document-link copy-file-id :file)]

    ; Copy the file physically
    (io/copy-file! filepath copy-filepath)

    ; Add the new file document to the "files" collection
    (local-db/add-document! "files" copy-file-document)

    ; Update the ancestor directories content-size
    (engine/update-path-content-size! destination-directory-id filesize +)

    ; Update file document modify data
    (let [modify-props (user/request->modify-props request)]
         (local-db/update-document! "files" file-id merge modify-props))

    ; Add the new file link to destination-directory document
    (engine/attach-item! env {:destination-directory-id destination-directory-id
                              :selected-item            copy-file-link})))

(defn- copy-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) action-props
  ;  {:copy-item-suffix (string)
  ;   :destination-directory-id (string)
  ;   :selected-item (map)
  ;    {:directory/id (string)}}
  ;
  ; @return (string)
  [{:keys [request] :as env}
   {:keys [copy-item-suffix destination-directory-id directory-subdirectories-alias-list selected-item]
     :as   action-props}]
  (let [directory-id            (db/document-link->document-id selected-item)
        directory-document      (local-db/get-document "directories" directory-id)
        directory-alias         (get directory-document :alias)
        copy-directory-id       (random/generate-string)
        copy-directory-alias    (directory-alias->copy-directory-alias directory-alias directory-subdirectories-alias-list copy-item-suffix)
        copy-directory-document (a/sub-prot env [destination-directory-id copy-directory-id directory-document]
                                            engine/directory-document-prototype)
        copy-directory-document (merge copy-directory-document {:alias copy-directory-alias})
        copy-directory-link     (db/document-id->document-link copy-directory-id :directory)]

    ; Add the new directory document to the "directories" collection
    (local-db/add-document! "directories" copy-directory-document)

    ; Add the new directory link to the destination-directory document
    (engine/attach-item! env {:destination-directory-id destination-directory-id
                              :selected-item            copy-directory-link})

    ; Update directory document modify data
    (let [modify-props (user/request->modify-props request)]
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

(pathom/reg-handler! :copy-items! copy-items!)



;; -- Move functions ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- move-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:destination-directory-id (string)
  ;   :selected-item (map)
  ;   :source-directory-id (string)}
  ;
  ; @return (string)
  [{:keys [request] :as env}
   {:keys [destination-directory-id selected-item source-directory-id]}]
  (let [file-id  (db/document-link->document-id selected-item)
        filesize (local-db/get-document-item "files" file-id :filesize)]

       ; Remove item link from the source-directory document
       (engine/detach-item! env {:source-directory-id source-directory-id
                                 :selected-item       selected-item})

       ; Update the ancestor directories content-size
       (engine/update-path-content-size! source-directory-id      filesize -)
       (engine/update-path-content-size! destination-directory-id filesize +)

       ; Add item link to the destination-directory document
       (engine/attach-item! env {:destination-directory-id destination-directory-id
                                 :selected-item            selected-item})

       ; Update file document modify data
       (let [modify-props (user/request->modify-props request)]
            (local-db/update-document! "files" file-id merge modify-props))

       (return "File moved")))

(defn- move-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:destination-directory-id (string)
  ;   :selected-item (map)
  ;   :source-directory-id (string)}
  ;
  ; @return (string)
  [{:keys [request] :as env}
   {:keys [destination-directory-id selected-item source-directory-id]}]

  ; Remove item link from the source-directory document
  (engine/detach-item! env {:source-directory-id source-directory-id
                            :selected-item       selected-item})

  ; Add item link to the destination-directory document
  (engine/attach-item! env {:destination-directory-id destination-directory-id
                            :selected-item            selected-item})

  ; Update directory documents modify data
  (let [modify-props (user/request->modify-props request)]
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

(pathom/reg-handler! :move-items! move-items!)
