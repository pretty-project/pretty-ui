
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.resolvers
    (:require [com.wsscode.pathom3.connect.operation    :refer [defresolver]]
              [extensions.clients.client-lister.helpers :as client-lister.helpers]
              [mongo-db.api                             :as mongo-db]
              [pathom.api                               :as pathom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-items-f
  [env _]
  (let [get-pipeline   (client-lister.helpers/env->get-pipeline   env)
        count-pipeline (client-lister.helpers/env->count-pipeline env)]
       {:documents      (mongo-db/get-documents-by-pipeline   "clients" get-pipeline)
        :document-count (mongo-db/count-documents-by-pipeline "clients" count-pipeline)}))

(defresolver get-items
             [env resolver-props]
             {:clients.client-lister/get-items (get-items-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def HANDLERS [get-items])

(pathom/reg-handlers! ::handlers HANDLERS)
