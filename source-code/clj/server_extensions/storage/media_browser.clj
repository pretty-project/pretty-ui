
(ns server-extensions.storage.media-browser
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.validator :as validator]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [x.server-core.api    :as a]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [server-extensions.storage.engine      :as engine]
              [server-plugins.item-browser.api       :as item-browser]))



;; -- Resolver ----------------------------------------------------------------
;; ----------------------------------------------------------------------------



;; ----------------------------------------------------------------------------
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
               (validator/validate-data document))))

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



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-media-item get-media-items])

(pathom/reg-handlers! ::handlers HANDLERS)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot [:item-browser/initialize! :storage :media {:default-item-id engine/ROOT-DIRECTORY-ID
                                                               :search-keys [:alias]}]})
