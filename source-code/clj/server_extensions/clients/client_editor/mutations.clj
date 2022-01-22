
(ns server-extensions.clients.client-editor.mutations
    (:require [mid-fruits.candy :refer [param return]]
              [mongo-db.api     :as mongo-db]
              [pathom.api       :as pathom]
              [prototypes.api   :as prototypes]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]))



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
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [_ {:keys [item-id]}]
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
(def HANDLERS [delete-client-item! duplicate-client-item! save-client-item! undo-delete-client-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
