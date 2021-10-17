
(ns extensions.media-storage.engine
    (:require [app-fruits.window    :as window]
              [mid-fruits.candy     :refer [param return]]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.io        :as io]
              [mid-fruits.map       :as map]
              [mid-fruits.reader    :as reader]
              [mid-fruits.string    :as string]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-dictionary.api :as dictionary]
              [x.app-elements.api   :as elements]
              [x.app-media.api      :as media]
              [x.app-sync.api       :as sync]
              [x.app-tools.api      :as tools]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
(def DOWNLOAD-DIRECTORY-DATA-PARAMS
     [:directory/alias :directory/content-size :directory/path
      {:directory/items [:directory/alias :directory/id :directory/created-at
                         :directory/content-size :directory/items-count :directory/modified-at
                         :file/alias :file/id :file/filename :file/filesize
                         :file/modified-at :file/uploaded-at]}])

; @constant (keyword)
(def ROOT-DIRECTORY-ID :home)

; @constant (map)
(def ROOT-DIRECTORY-ENTITY (db/item-id->document-entity ROOT-DIRECTORY-ID :directory))

; @constant (vector)
;  A szinkronizációk alkalmával a gyökér könyvtár adatait is szükséges frissíteni
;  a felhasznált tárhely-kapacitás értékének esetleges változása miatt.
(def ROOT-DIRECTORY-QUERY [{ROOT-DIRECTORY-ENTITY DOWNLOAD-DIRECTORY-DATA-PARAMS}])



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-directory-data-query-question
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @example
  ;  (engine/download-directory-data-query-question :my-directory)
  ;  => {[:directory/id "my-directory"] [:directory/alias ...]}
  ;
  ; @return (map)
  [directory-id]
  (let [directory-entity (db/item-id->document-entity directory-id)]
       {directory-entity DOWNLOAD-DIRECTORY-DATA-PARAMS}))

(defn subdirectory-prop-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (keyword) subdirectory-id
  ; @param (keyword) prop-id
  ;
  ; @return (vector)
  [directory-id subdirectory-id prop-id]
  (db/path ::directories directory-id :directory/subdirectories subdirectory-id prop-id))

(defn file-prop-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (keyword) file-id
  ; @param (keyword) prop-id
  ;
  ; @return (vector)
  [directory-id file-id prop-id]
  (db/path ::directories directory-id :directory/files file-id prop-id))

(defn directory-subdirectories->filtered-subdirectories
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) directory-subdirectories
  ; @param (string) filter-phrase
  ;
  ; @example
  ;  (engine/directory-subdirectories->filtered-subdirectories {:my-subdirectory   {:subdirectory/alias "My subdirectory"}
  ;                                                             :your-subdirectory {:subdirectory/alias "Your subdirectory"}}
  ;                                                            "my s")
  ;  => {:my-subdirectory {:subdirectory/alias "My subdirectory"}}
  ;
  ; @return (map)
  [directory-subdirectories filter-phrase]
  (map/filter-values-by directory-subdirectories #(string/starts-with? % filter-phrase {:case-sensitive? false})
                                                 #(get % :directory/alias)))

(defn directory-files->filtered-files
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) directory-files
  ; @param (string) filter-phrase
  ;
  ; @example
  ;  (engine/directory-files->filtered-files {:my-file   {:file/alias "My file"}
  ;                                           :your-file {:file/alias "Your file"}}
  ;                                          "my f")
  ;  => {:my-file {:file/alias "My file"}}
  ;
  ; @return (map)
  [directory-files filter-phrase]
  (map/filter-values-by directory-files #(string/starts-with? % filter-phrase {:case-sensitive? false})
                                        #(get % :file/alias)))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn namespace->query-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) namespace
  ;
  ; @example
  ;  (engine/namespace->query-id :extensions.media-storage.file-storage)
  ;  => :extensions.media-storage.file-storage/synchronize!
  ;
  ; @return (keyword)
  [namespace]
  (keyword/add-namespace namespace :synchronize!))

(defn namespace->partition-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) namespace
  ;
  ; @example
  ;  (engine/namespace->partition-id :extensions.media-storage.file-storage)
  ;  => :extensions.media-storage.file-storage/primary
  ;
  ; @return (namespaced keyword)
  [namespace]
  (keyword/add-namespace namespace :primary))

(defn file-dex->element-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) file-dex
  ; @param (keyword) namespace
  ;
  ; @example
  ;  (engine/file-dex->element-id 4 :file-uploader)
  ;  => :file-uploader/file--4
  ;
  ; @return (keyword)
  [file-dex namespace]
  (let [base (keyword/add-namespace namespace :file)]
       (keyword/append base (str file-dex) "--")))

(defn item-id->file-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @example
  ;  (engine/item-id->file-dex :file--4)
  ;  => "4"
  ;
  ; @return (string)
  [item-id]
  (let [file-dex (string/after-last-occurence (str item-id) "--")]
       (string/to-integer file-dex)))

(defn element-id->file-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @example
  ;  (engine/element-id->file-dex :file-uploader/file--4)
  ;  => "4"
  ;
  ; @return (string)
  [element-id]
  (let [item-id (keyword/get-name element-id)]
       (item-id->file-dex item-id)))

(defn- item-id->element-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; XXX#2191
  ;  Előfordulhat, hogy egy surface UI-elemen és egy popup UI-elem egyszerre vannak
  ;  kirenderelve ugyanazzal az azonosítóval rendelkező fájlok vagy almappák.
  ;  Ezért a fájlok és almappák azonosítóinak egyezéséből következő névütközés
  ;  elkerülése végett a kirenderelt fájl és almappa elemek azonosítói névtérrel
  ;  vannak ellátva.
  ;
  ; @param (keyword) item-id
  ; @param (keyword) namespace
  ;
  ; @example
  ;  (item-id->element-id :my-file :file-browser)
  ;  => :file-browser/my-file
  ;
  ; @return (keyword)
  [item-id namespace]
  (keyword/add-namespace namespace item-id))

(defn- element-id->item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; XXX#2191
  ;
  ; @param (keyword) element-id
  ;
  ; @example
  ;  (element-id->item-id :file-browser/my-file)
  ;  => :my-file
  ;
  ; @return (keyword)
  [element-id]
  (keyword/get-name element-id))

(defn- view-props->root-level?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:directory-path (maps in vector)}
  ;
  ; @return (boolean)
  [{:keys [directory-path]}]
  (empty? directory-path))

(defn- view-props->ordered-subdirectories
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:filtered-subdirectories (map)
  ;   :order-by (keyword)}
  ;
  ; @return (keywords in vector)
  [{:keys [filtered-subdirectories order-by]}]
  (case order-by
        :by-date (map/get-ordered-keys-by filtered-subdirectories string/abc? :directory/modified-at)
        :by-name (map/get-ordered-keys-by filtered-subdirectories string/abc? :directory/alias)
        :by-size (map/get-ordered-keys-by filtered-subdirectories <           :directory/content-size)
                 (return                  filtered-subdirectories)))

(defn- view-props->ordered-files
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:filtered-files (map)
  ;   :order-by (keyword)}
  ;
  ; @return (keywords in vector)
  [{:keys [filtered-files order-by]}]
  (case order-by
        :by-date (map/get-ordered-keys-by filtered-files string/abc? :file/modified-at)
        :by-name (map/get-ordered-keys-by filtered-files string/abc? :file/alias)
        :by-size (map/get-ordered-keys-by filtered-files <           :file/filesize)
                 (return                  filtered-files)))

(defn- directory-data->modified-directory-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) directory-data
  ;  {:directory/items (maps in vector)}
  ;
  ; @example
  ;  (directory-data->modified-directory-data {:directory/alias ":my-storage"
  ;                                            :directory/path  []
  ;                                            :directory/items [{...} {...}]})
  ;  =>
  ;  {:directory/alias          ":my-storage"
  ;   :directory/path           []
  ;   :directory/files          {:my-file {...}}
  ;   :directory/subdirectories {:my-subdirectory {...}}}
  ;
  ; @return (map)
  [directory-data]
  (let [directory-items          (get directory-data :directory/items)
        exploded-items           (db/explode-collection directory-items)
        directory-files          (get exploded-items :file)
        directory-subdirectories (get exploded-items :directory)]
       (-> (param directory-data)
           (assoc  :directory/files          (db/collection->map directory-files))
           (assoc  :directory/subdirectories (db/collection->map directory-subdirectories))
           (dissoc :directory/items))))

(defn- file-props->thumbnail-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) file-props
  ;  {:file/filename (string)}
  ;
  ; @return (string)
  [{:file/keys [filename]}]
  (if (io/filename->image? filename)
      (media/filename->media-storage-uri filename)))



;; -- Directory subscriptions -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-directory-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @return (map)
  [db [_ directory-id]]
  (get-in db (db/path ::directories directory-id)))

(defn get-directory-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (keyword) prop-id
  ;
  ; @usage
  ;  (r engine/get-directory-prop db :my-directory :directory/alias)
  ;
  ; @return (map)
  [db [_ directory-id prop-id]]
  (get-in db (db/path ::directories directory-id prop-id)))

(defn get-directory-alias
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @return (string)
  [db [_ directory-id]]
  (let [stored-alias    (r get-directory-prop db directory-id :directory/alias)
        directory-alias (reader/string->mixed stored-alias)]
       (r components/get-metamorphic-value db {:value directory-alias})))

(defn directory-exists?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @return (boolean)
 [db [_ directory-id]]
 (let [directory-alias (r get-directory-alias db directory-id)]
      (string/nonempty? directory-alias)))

(defn directory-not-exists?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @return (boolean)
 [db [_ directory-id]]
 (not (r directory-exists? db directory-id)))

(defn get-directory-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @return (maps in vector)
  [db [_ directory-id]]
  (r get-directory-prop db directory-id :directory/path))

(defn get-directory-subdirectories
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @return (maps in vector)
  [db [_ directory-id]]
  (r get-directory-prop db directory-id :directory/subdirectories))

(defn get-directory-subdirectories-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @return (integer)
  [db [_ directory-id]]
  (let [directory-subdirectories (r get-directory-subdirectories db directory-id)]
       (count directory-subdirectories)))

(defn get-filtered-subdirectories
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (string) filter-phrase
  ;
  ; @return (maps in vector)
  [db [_ directory-id filter-phrase]]
  (let [directory-subdirectories (r get-directory-subdirectories db directory-id)]
       (directory-subdirectories->filtered-subdirectories directory-subdirectories filter-phrase)))

(defn get-directory-files
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @return (maps in vector)
  [db [_ directory-id]]
  (r get-directory-prop db directory-id :directory/files))

(defn get-directory-files-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @return (integer)
  [db [_ directory-id]]
  (let [directory-files (r get-directory-files db directory-id)]
       (count directory-files)))

(defn get-filtered-files
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (string) filter-phrase
  ;
  ; @return (maps in vector)
  [db [_ directory-id filter-phrase]]
  (let [directory-files (r get-directory-files db directory-id)]
       (directory-files->filtered-files directory-files filter-phrase)))

(defn directory-contains-subdirectories?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @return (boolean)
  [db [_ directory-id]]
  (let [directory-subdirectories (r get-directory-subdirectories db directory-id)]
       (map/nonempty? directory-subdirectories)))

(defn directory-contains-files?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @return (boolean)
  [db [_ directory-id]]
  (let [directory-files (r get-directory-files db directory-id)]
       (map/nonempty? directory-files)))

(defn directory-contains-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @return (boolean)
  [db [_ directory-id]]
  (boolean (or (r directory-contains-files?          db directory-id)
               (r directory-contains-subdirectories? db directory-id))))

(defn directory-empty?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @return (boolean)
  [db [_ directory-id]]
  (let [directory-contains-items? (r directory-contains-items? db directory-id)]
       (not directory-contains-items?)))

(defn get-file-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (keyword) file-id
  ;
  ; @return (map)
  [db [_ directory-id file-id]]
  (let [directory-props (r get-directory-props db directory-id)]
       (get-in directory-props [:directory/files file-id])))

(defn get-file-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (keyword) file-id
  ; @param (keyword) prop-id
  ;
  ; @return (map)
  [db [_ directory-id file-id prop-id]]
  (let [directory-props (r get-directory-props db directory-id)]
       (get-in directory-props [:directory/files file-id prop-id])))

(defn get-subdirectory-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (keyword) subdirectory-id
  ;
  ; @return (map)
  [db [_ directory-id subdirectory-id]]
  (let [directory-props (r get-directory-props db directory-id)]
       (get-in directory-props [:directory/subdirectories subdirectory-id])))

(defn get-subdirectory-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (keyword) subdirectory-id
  ; @param (keyword) prop-id
  ;
  ; @return (map)
  [db [_ directory-id subdirectory-id prop-id]]
  (let [directory-props (r get-directory-props db directory-id)]
       (get-in directory-props [:directory/subdirectories subdirectory-id prop-id])))

(defn get-file-link
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (keyword) file-id
  ;
  ; @return (string)
  [db [_ directory-id file-id]]
  (let [filename  (r get-file-prop db directory-id file-id :file/filename)
        uri-base  (window/get-uri-base)
        file-link (media/filename->media-storage-uri filename)]
       (str uri-base file-link)))



;; -- Storage subscriptions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-storage-used-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (r get-directory-prop db ROOT-DIRECTORY-ID :directory/content-size))

(defn get-storage-total-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (r a/get-storage-detail db :storage-capacity))

(defn get-storage-free-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (B)
  [db _]
  (let [used-capacity  (r get-storage-used-capacity  db)
        total-capacity (r get-storage-total-capacity db)]
       (- total-capacity used-capacity)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-rendered-directory-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) namespace
  ;
  ; @return (keyword)
  [db [_ namespace]]
  (let [partition-id (namespace->partition-id namespace)]
       (get-in db (db/meta-item-path partition-id :rendered-directory-id))))

(defn- get-parent-directory-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) namespace
  ;
  ; @return (keyword)
  [db [_ namespace]]
  (let [rendered-directory-id (r get-rendered-directory-id db namespace)
        directory-path        (r get-directory-path        db rendered-directory-id)
        parent-directory      (last directory-path)
        parent-directory-id   (get  parent-directory :directory/id)]
       (keyword parent-directory-id)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-rendered-directory-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) namespace
  ; @param (keyword) directory-id
  ;
  ; @return (keyword)
  [db [_ namespace directory-id]]
  (let [partition-id (namespace->partition-id namespace)]
       (assoc-in db (db/meta-item-path partition-id :rendered-directory-id)
                    (param directory-id))))

(defn- handle-directory-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) directory-data
  ;  {:directory/items (maps in vector)}
  ;
  ; @return (map)
  [db [_ directory-id directory-data]]
  (if-let [directory-items (get directory-data :directory/items)]
          (reduce (fn [db directory-item]
                      (let [document-id  (db/document->document-id     directory-item)
                            data-item-id (db/document-id->data-item-id document-id)
                            data-item    (db/document->data-item       directory-item)]
                           (if (db/document->namespace? directory-item :file)
                               (assoc-in db (db/path ::files data-item-id)
                                            (param data-item))
                               (assoc-in db (db/path ::directories data-item-id)
                                            (param data-item)))))
                  (param db)
                  (param directory-items))
          (return db)))

(defn- handle-directory-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ; @param (map) directory-data
  ;
  ; @return (map)
  [db [_ directory-id directory-data]]
  ; @sample (map) directory-data
  ;  {:directory/alias ":my-storage"
  ;   :directory/path  []
  ;   :directory/items [{...} {...}]}
  ;
  ; @sample (map) modified-directory-data
  ;  {:directory/alias ":my-storage"
  ;   :directory/path  []
  ;   :directory/files {:my-file {...}}
  ;   :directory/subdirectories {:my-directory {...}}}
  (let [modified-directory-data (directory-data->modified-directory-data directory-data)]
       (assoc-in db (db/path ::directories directory-id)
                    (param modified-directory-data))))

(defn handle-request-response!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (map)
  [db [_ request-id]]
  ; @sample (map) request-response
  ;  {[:directory/id "home"] {:directory/alias ":my-storage"
  ;                           :directory/path  []
  ;                           :directory/items [{...} {...}]}}
  (let [request-response (r sync/get-request-response db request-id)]
       (reduce-kv (fn [db query-key query-answer]
                      (if (db/document-entity? query-key)
                          (let [directory-id (db/document-entity->item-id query-key)]
                               (r handle-directory-data! db directory-id query-answer))
                          (return db)))
                  (param db)
                  (param request-response))))

(a/reg-event-db :media-storage/handle-request-response! handle-request-response!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :media-storage/copy-file-link!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filename
  (fn [{:keys [db]} [_ filename]]
      (let [uri-base  (window/get-uri-base)
            file-link (media/filename->media-storage-uri filename)]
            ; TODO
            ; file-link (r get-file-link db directory-id file-id)
           [:x.app-tools.clipboard/copy-to! (str uri-base file-link)])))

(a/reg-event-fx
  :media-storage/->file-alias-edited
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) action-id
  ; @param (map) action-props
  ;  {:source-directory-id (keyword)
  ;   :file-id (keyword)}
  (fn [{:keys [db]} [_ action-id {:keys [source-directory-id file-id]}]]
      (let [directory-entity   (db/item-id->document-entity source-directory-id :directory)
            file-id            (name file-id)
            file-alias         (r tools/get-editor-value db :media-storage/alias-editor)
            updated-file-props {:alias         file-alias}
            mutation-props     {:file-id       file-id
                                :updated-props updated-file-props}
            query-action       (sync/query-action "media/update-file!" mutation-props)
            query-question     {directory-entity DOWNLOAD-DIRECTORY-DATA-PARAMS}]
           [:x.app-sync/send-query!
             action-id
             {:on-success [:media-storage/handle-request-response! action-id]
              :query      (sync/append-to-query ROOT-DIRECTORY-QUERY query-action query-question)
              :uri        (param "/media/query")}])))

(a/reg-event-fx
  :media-storage/->subdirectory-alias-edited
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) action-id
  ; @param (map) action-props
  ;  {:source-directory-id (keyword)
  ;   :subdirectory-id (keyword)}
  (fn [{:keys [db]} [_ action-id {:keys [source-directory-id subdirectory-id]}]]
      (let [directory-entity           (db/item-id->document-entity source-directory-id :directory)
            subdirectory-id            (name subdirectory-id)
            subdirectory-alias         (r tools/get-editor-value db :media-storage/alias-editor)
            updated-subdirectory-props {:alias         subdirectory-alias}
            mutation-props             {:directory-id  subdirectory-id
                                        :updated-props updated-subdirectory-props}
            query-action               (sync/query-action "media/update-directory!" mutation-props)
            query-question             {directory-entity DOWNLOAD-DIRECTORY-DATA-PARAMS}]
           [:x.app-sync/send-query!
             action-id
             {:on-success [:media-storage/handle-request-response! action-id]
              :query      (sync/append-to-query ROOT-DIRECTORY-QUERY query-action query-question)
              :uri        (param "/media/query")}])))

(a/reg-event-fx
  :media-storage/create-subdirectory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) action-id
  ; @param (map) action-props
  ;  {:destination-directory-id (keyword)}
  (fn [{:keys [db]} [_ action-id {:keys [destination-directory-id]}]]
      (let [directory-entity   (db/item-id->document-entity destination-directory-id :directory)
            subdirectory-alias (r tools/get-editor-value db :media-storage/alias-editor)
            mutation-props     {:destination-directory-id (name  destination-directory-id)
                                :directory-alias          (param subdirectory-alias)}
            query-action       (sync/query-action "media/create-directory!" mutation-props)
            query-question     {directory-entity DOWNLOAD-DIRECTORY-DATA-PARAMS}]
           [:media-storage/test]
           [:x.app-sync/send-query!
             action-id
             {:on-success [:media-storage/handle-request-response! action-id]
              :query      (sync/append-to-query ROOT-DIRECTORY-QUERY query-action query-question)
              :uri        (param "/media/query")}])))

(a/reg-event-fx
  :media-storage/delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) action-id
  ; @param (map) action-props
  ;  {:source-directory-id (keyword)
  ;   :selected-item (map)
  ;    {:directory/id (string)
  ;     :file/id (string)}}
  (fn [{:keys [db]} [_ action-id {:keys [source-directory-id selected-item]}]]
      (let [directory-entity (db/item-id->document-entity source-directory-id :directory)
            mutation-props   {:source-directory-id (name source-directory-id)
                              :selected-items      [selected-item]}
            query-action     (sync/query-action "media/delete-items!" mutation-props)
            query-question   {directory-entity DOWNLOAD-DIRECTORY-DATA-PARAMS}]
           [:x.app-sync/send-query!
             action-id
             {:on-success [:media-storage/handle-request-response! action-id]
              :query      (sync/append-to-query ROOT-DIRECTORY-QUERY query-action query-question)
              :uri        (param "/media/query")}])))

(a/reg-event-fx
  :media-storage/copy-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) action-id
  ; @param (map) action-props
  ;  {:destination-directory-id (keyword)
  ;   :selected-item (map)
  ;    {:directory/id (string)
  ;     :file/id (string)}}
  (fn [{:keys [db]} [_ action-id {:keys [destination-directory-id selected-item]}]]
      (let [directory-entity (db/item-id->document-entity destination-directory-id :directory)
            copy-item-suffix (r dictionary/look-up db :copy)
            mutation-props   {:copy-item-suffix         (string/lowercase copy-item-suffix)
                              :destination-directory-id (name destination-directory-id)
                              :selected-items           [selected-item]}
            query-action     (sync/query-action "media/copy-items!" mutation-props)
            query-question   {directory-entity DOWNLOAD-DIRECTORY-DATA-PARAMS}]
           [:x.app-sync/send-query!
             action-id
             {:on-success [:media-storage/handle-request-response! action-id]
              :query      (sync/append-to-query ROOT-DIRECTORY-QUERY query-action query-question)
              :uri        (param "/media/query")}])))

(a/reg-event-fx
  :media-storage/move-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) action-id
  ; @param (map) action-props
  ;  {:destination-directory-id (keyword)
  ;   :selected-item (map)
  ;    {:directory/id (string)
  ;     :file/id (string)}}
  (fn [{:keys [db]} [_ action-id {:keys [destination-directory-id selected-item source-directory-id]}]]
      (let [directory-entity (db/item-id->document-entity source-directory-id :directory)
            mutation-props   {:destination-directory-id (name destination-directory-id)
                              :selected-items           [selected-item]
                              :source-directory-id      (name source-directory-id)}
            query-action     (sync/query-action "media/move-items!" mutation-props)
            query-question   {directory-entity DOWNLOAD-DIRECTORY-DATA-PARAMS}]
           [:x.app-sync/send-query!
             action-id
             {:on-success [:media-storage/handle-request-response! action-id]
              :query      (sync/append-to-query ROOT-DIRECTORY-QUERY query-action query-question)
              :uri        (param "/media/query")}])))

(a/reg-event-fx
  :media-storage/download-directory-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) query-id
  ; @param (map) query-props
  ;  {:directory-id (keyword)
  ;   :on-sent (metamorphic-event)(opt)
  ;   :on-success (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ query-id {:keys [directory-id on-sent on-success]}]]
      (let [directory-entity (db/item-id->document-entity directory-id :directory)
            query-question   {directory-entity DOWNLOAD-DIRECTORY-DATA-PARAMS}]
           [:x.app-sync/send-query!
             query-id
             {:on-sent    (param on-sent)
              :on-success {:dispatch-n [[:media-storage/handle-request-response! query-id]
                                        (param on-success)]}
              :query      (sync/append-to-query ROOT-DIRECTORY-QUERY query-question)
              :uri        (param "/media/query")}])))
