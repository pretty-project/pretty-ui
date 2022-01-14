
(ns server-extensions.storage.directory-browser
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.vector :as vector]
              [mongo-db.api      :as mongo-db]
              [pathom.api        :as pathom]
              [prototypes.api    :as prototypes]
              [x.server-core.api :as a]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [server-extensions.storage.engine      :as engine]
              [server-plugins.item-browser.api       :as item-browser]
              [server-plugins.item-lister.api        :as item-lister]))



;; -- Attach/detach item functions --------------------------------------------
;; ----------------------------------------------------------------------------

(defn attach-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (string) directory-id
  ; @param (namespaced map) item-link
  ;  {:namespace/id (string)}
  ;
  ; @return (namespaced map)
  [{:keys [request]} directory-id item-link]
  (mongo-db/apply-document! "directories" directory-id #(update % :items vector/conj-item item-link)
                            {:prototype-f #(prototypes/updated-document-prototype request :directory %)}))

(defn detach-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (string) directory-id
  ; @param (namespaced map) item-link
  ;  {:namespace/id (string)}
  ;
  ; @return (namespaced map)
  [{:keys [request]} directory-id item-link]
  (mongo-db/apply-document! "directories" directory-id #(update % :items vector/remove-item item-link)
                            {:prototype-f #(prototypes/updated-document-prototype request :directory %)}))



;; -- Resolver ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-directory-items-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:document-count (integer)
  ;   :documents (namespaced maps in vector)}}
  [env _]
  
  (let [directory-id (pathom/env->param env :item-id)])

  (let [get-pipeline   (item-lister/env->get-pipeline   env :directories :directory)
        count-pipeline (item-lister/env->count-pipeline env :directories :directory)]
       {:documents      (mongo-db/get-documents-by-pipeline   "directories" get-pipeline)
        :document-count (mongo-db/count-documents-by-pipeline "directories" count-pipeline)}))

(defresolver get-directory-items
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:storage/get-directory-items (map)
             ;    {:document-count (integer)
             ;     :documents (namespaced maps in vector)}}
             [env resolver-props]
             {:storage/get-directory-items (get-directory-items-f env resolver-props)})



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-directory-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (namespaced map) directory-item
  ;
  ; @return (namespaced map)
  [{:keys [request]} directory-item]
  (mongo-db/insert-document! "directories" directory-item
                             {:prototype-f #(prototypes/added-document-prototype request :directory %)}))

(defmutation add-directory-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) directory-item
             ;
             ; @return (namespaced map)
             [env directory-item]
             {::pathom.co/op-name 'storage/add-directory-item!}
             (add-directory-item-f env directory-item))

(defn add-file-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (namespaced map) file-item
  ;
  ; @return (namespaced map)
  [{:keys [request]} file-item]
  (mongo-db/insert-document! "files" file-item
                             {:prototype-f #(prototypes/added-document-prototype request :file %)}))

(defmutation add-file-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) file-item
             ;
             ; @return (namespaced map)
             [env file-item]
             {::pathom.co/op-name 'storage/add-file-item!}
             (add-file-item-f env file-item))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-directory-items add-directory-item! add-file-item!])

(pathom/reg-handlers! ::handlers HANDLERS)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot [:item-browser/initialize! :storage :directory {:default-item-id engine/ROOT-DIRECTORY-ID
                                                                   :search-keys [:alias]}]})
