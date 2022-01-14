
(ns server-extensions.clients.client-editor
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.validator :as validator]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [prototypes.api       :as prototypes]
              [x.server-core.api    :as a]
              [x.server-db.api      :as db]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [server-plugins.item-editor.api        :as item-editor]))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-client-item-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  [env _]
  (let [item-id (pathom/env->param env :item-id)]
       (if-let [document (mongo-db/get-document-by-id "clients" item-id)]
               (validator/validate-data document))))

(defresolver get-client-item
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:clients/get-client-item (namespaced map)}
             [env resolver-props]
             {:clients/get-client-item (get-client-item-f env resolver-props)})



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation undo-delete-client-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) client-item
             ;
             ; @return (namespaced map)
             [_ client-item]
             {::pathom.co/op-name 'clients/undo-delete-client-item!}
             (mongo-db/insert-document! "clients" client-item))

(defmutation save-client-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) client-item
             ;
             ; @return (namespaced map)
             [{:keys [request]} client-item]
             {::pathom.co/op-name 'clients/save-client-item!}
             (mongo-db/save-document! "clients" client-item
                                      {:prototype-f #(prototypes/updated-document-prototype request :client %)}))

(defmutation delete-client-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [{:keys [item-id]}]
             {::pathom.co/op-name 'clients/delete-client-item!}
             (mongo-db/remove-document! "clients" item-id))

(defmutation duplicate-client-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) client-item
             ;
             ; @return (namespaced map)
             [{:keys [request]} client-item]
             {::pathom.co/op-name 'clients/duplicate-client-item!}
             (mongo-db/insert-document! "clients" client-item
                                        {:prototype-f #(prototypes/duplicated-document-prototype request :client %)}))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-client-item! duplicate-client-item! get-client-item
               save-client-item!   undo-delete-client-item!])

(pathom/reg-handlers! ::handlers HANDLERS)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot [:item-editor/initialize! :clients :client {:suggestion-keys [:city]}]})
