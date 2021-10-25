
(ns extensions.clients.pathom-handlers
    (:require [mongo-db.api :as mongo-db]
      [pathom.env   :as env]
      [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))


(def collection-name "clients")

;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defresolver get-clients
             ; @param (map) env
             ; @param (?) ?
             ;
             ; @return (map)
             ;  {:clients/get-clients (map)
             ;    {:item-count (integer)
             ;     :items (maps in vector)}}
             [env _]
             {:clients/get-clients
              (let [pipeline (mongo-db/search-props->pipeline)]
                {:item-count (mongo-db/count-documents-by-pipeline collection-name pipeline)
                 :items      (mongo-db/get-documents-by-pipeline   collection-name pipeline)})})

(defresolver get-client
             ; @param (map) env
             ; @param (?) ?
             ;
             ; @return (map)
             ;  {:clients/get-client (map)
             ;    (map)}}
             [env {:keys [client/id]}]
             {:clients/get-client (mongo-db/get-document-by-id collection-name id)})


; @constant (vector)
(def HANDLERS [get-client
               get-clients])

(a/dispatch [:pathom/reg-handlers! HANDLERS])
