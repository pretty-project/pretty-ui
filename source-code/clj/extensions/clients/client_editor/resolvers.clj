
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-editor.resolvers
    (:require [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [mid-fruits.candy                      :refer [param return]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env _]
  (let [item-id (pathom/env->param env :item-id)]
       (mongo-db/get-document-by-id "clients" item-id)))

(defresolver get-item
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env resolver-props]
             {:clients.client-editor/get-item (get-item-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-item])

(pathom/reg-handlers! ::handlers HANDLERS)
