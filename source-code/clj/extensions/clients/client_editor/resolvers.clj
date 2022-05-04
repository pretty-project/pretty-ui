
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-editor.resolvers
    (:require [com.wsscode.pathom3.connect.operation    :as pathom.co :refer [defresolver defmutation]]
              [extensions.clients.client-editor.helpers :as client-editor.helpers]
              [mongo-db.api                             :as mongo-db]
              [pathom.api                               :as pathom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-f
  [env _]
  ; XXX#7601
  (let [item-id (pathom/env->param env :item-id)]
       (if-let [client-item (mongo-db/get-document-by-id "clients" item-id)]
               (client-editor.helpers/client-item<-name-field env client-item))))

(defresolver get-item
             [env resolver-props]
             {:clients.client-editor/get-item (get-item-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def HANDLERS [get-item])

(pathom/reg-handlers! ::handlers HANDLERS)
