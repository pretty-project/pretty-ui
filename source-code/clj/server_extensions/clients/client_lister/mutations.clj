
(ns server-extensions.clients.client-lister.mutations
    (:require [mid-fruits.candy :refer [param return]]
              [mongo-db.api     :as mongo-db]
              [pathom.api       :as pathom]
              [prototypes.api   :as prototypes]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]))



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation undo-delete-client-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) mutation-props
             ;  {:items (namespaced maps in vector)}
             ;
             ; @return (namespaced maps in vector)
             [_ {:keys [items]}]
             {::pathom.co/op-name 'clients/undo-delete-client-items!}
             (mongo-db/insert-documents! "clients" items))

(defmutation delete-client-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (strings in vector)
             [_ {:keys [item-ids]}]
             {::pathom.co/op-name 'clients/delete-client-items!}
             (mongo-db/remove-documents! "clients" item-ids))

(defmutation undo-duplicate-client-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (strings in vector)
             [_ {:keys [item-ids]}]
             {::pathom.co/op-name 'clients/undo-duplicate-client-items!}
             (mongo-db/remove-documents! "clients" item-ids))

(defmutation duplicate-client-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (namespaced maps in vector)
             [{:keys [request]} {:keys [item-ids]}]
             {::pathom.co/op-name 'clients/duplicate-client-items!}
             (mongo-db/duplicate-documents! "clients" item-ids
                                            {:prototype-f #(prototypes/duplicated-document-prototype request :client %)}))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-client-items! undo-delete-client-items! duplicate-client-items! undo-duplicate-client-items!])

(pathom/reg-handlers! ::handlers HANDLERS)
