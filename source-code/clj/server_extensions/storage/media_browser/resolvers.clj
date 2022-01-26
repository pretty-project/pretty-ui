
(ns server-extensions.storage.media-browser.resolvers
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.validator :as validator]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [com.wsscode.pathom3.connect.operation      :refer [defresolver]]
              [server-extensions.storage.capacity-handler :as capacity-handler]
              [server-plugins.item-browser.api            :as item-browser]))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  [env response-props]
  (let [item-id (pathom/env->param env :item-id)]
       (if-let [media-item (mongo-db/get-document-by-id "storage" item-id)]
               (if-let [capacity-details (capacity-handler/get-capacity-details)]
                       (let [media-item (merge media-item capacity-details)]
                            (validator/validate-data media-item))))))

(defresolver get-item
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:storage.media-browser/get-item (namespaced map)
             [env resolver-props]
             {:storage.media-browser/get-item (get-item-f env resolver-props)})

(defn get-items-f
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

(defresolver get-items
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:storage.media-lister/get-items (map)
             ;    {:document-count (integer)
             ;     :documents (namespaced maps in vector)}}
             [env resolver-props]
             {:storage.media-lister/get-items (get-items-f env resolver-props)})



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-item get-items])

(pathom/reg-handlers! ::handlers HANDLERS)
