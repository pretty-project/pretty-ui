
(ns server-extensions.storage.media-browser.handlers
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.validator :as validator]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [x.server-core.api    :as a]
              [com.wsscode.pathom3.connect.operation      :as pathom.co :refer [defresolver defmutation]]
              [server-extensions.storage.capacity-handler :as capacity-handler]
              [server-extensions.storage.engine           :as engine]
              [server-plugins.item-browser.api            :as item-browser]))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-media-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  [env response-props]
  (let [item-id (pathom/env->param env :item-id)]
       (if-let [document (mongo-db/get-document-by-id "storage" item-id)]
               (if-let [capacity-details (capacity-handler/get-capacity-details)]
                       (let [media-item (merge document capacity-details)]
                            (validator/validate-data media-item))))))

(defresolver get-media-item
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:storage/get-media-item (namespaced map)
             [env resolver-props]
             {:storage/get-media-item (get-media-item-f env resolver-props)})

(defn get-media-items-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:document-count (integer)
  ;   :documents (namespaced maps in vector)}}
  [env _]
  (let [get-pipeline   (item-browser/env->get-pipeline   env :storage :media)
        count-pipeline (item-browser/env->count-pipeline env :storage :media)]
       {:documents      (mongo-db/get-documents-by-pipeline   "storage" get-pipeline)
        :document-count (mongo-db/count-documents-by-pipeline "storage" count-pipeline)}))

(defresolver get-media-items
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:storage/get-media-items (map)
             ;    {:document-count (integer)
             ;     :documents (namespaced maps in vector)}}
             [env resolver-props]
             {:storage/get-media-items (get-media-items-f env resolver-props)})



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- create-directory-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;
  ; @return (namespaced map)
  [env {:keys [alias destination-id]}]
  (if-let [destination-item (mongo-db/get-document-by-id "storage" destination-id)]
          (let [destination-path (get  destination-item :media/path)
                directory-path   (conj destination-path {:media/id destination-id})
                directory-item {:media/alias alias :media/content-size 0 :media/description ""
                                :media/items []    :media/path directory-path
                                :media/mime-type "storage/directory"}]
               (if-let [{:media/keys [id]} (engine/insert-media-item! env directory-item)]
                       (engine/attach-media-item! env destination-id id)))))

(defmutation create-directory!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (?)
             [env mutation-props]
             {::pathom.co/op-name 'storage/create-directory!}
             (create-directory-f env mutation-props))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [create-directory! get-media-item get-media-items])

(pathom/reg-handlers! ::handlers HANDLERS)
