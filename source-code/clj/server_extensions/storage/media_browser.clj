
(ns server-extensions.storage.media-browser
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

(defn attach-media-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (string) directory-id
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [{:keys [request]} directory-id item-id]
  (mongo-db/apply-document! "storage" directory-id #(update % :media/items vector/conj-item {:media/id item-id})
                            {:prototype-f #(prototypes/updated-document-prototype request :media %)}))

(defn detach-media-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)}
  ; @param (string) directory-id
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [{:keys [request]} directory-id item-id]
  (mongo-db/apply-document! "storage" directory-id #(update % :media/items vector/remove-item {:media/id item-id})
                            {:prototype-f #(prototypes/updated-document-prototype request :media %)}))



;; -- Resolver ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
       ;(println (str get-pipeline))
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

(defn add-media-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (namespaced map) media-item
  ;
  ; @return (namespaced map)
  [{:keys [request]} media-item]
  (mongo-db/insert-document! "storage" media-item
                             {:prototype-f #(prototypes/added-document-prototype request :media %)}))

(defmutation add-media-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) media-item
             ;
             ; @return (namespaced map)
             [env media-item]
             {::pathom.co/op-name 'storage/add-media-item!}
             (add-media-item-f env media-item))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-media-items add-media-item!])

(pathom/reg-handlers! ::handlers HANDLERS)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot [:item-browser/initialize! :storage :media {:default-item-id engine/ROOT-DIRECTORY-ID
                                                               :search-keys [:alias]}]})
