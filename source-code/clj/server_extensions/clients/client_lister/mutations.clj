
(ns server-extensions.clients.client-lister.mutations
    (:require [mid-fruits.candy     :refer [param return]]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [x.server-locales.api :as locales]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]))



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation undo-delete-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) mutation-props
             ;  {:items (namespaced maps in vector)}
             ;
             ; @return (namespaced maps in vector)
             [_ {:keys [items]}]
             {::pathom.co/op-name 'clients.client-lister/undo-delete-items!}
             (mongo-db/insert-documents! "clients" items))

(defmutation delete-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (strings in vector)
             [_ {:keys [item-ids]}]
             {::pathom.co/op-name 'clients.client-lister/delete-items!}
             (mongo-db/remove-documents! "clients" item-ids))

(defmutation undo-duplicate-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (strings in vector)
             [_ {:keys [item-ids]}]
             {::pathom.co/op-name 'clients.client-lister/undo-duplicate-items!}
             (mongo-db/remove-documents! "clients" item-ids))

(defmutation duplicate-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (namespaced maps in vector)
             [{:keys [request]} {:keys [item-ids]}]
             {::pathom.co/op-name 'clients.client-lister/duplicate-items!}
             (let [name-order (locales/request->name-order request)]
                  (mongo-db/duplicate-documents! "clients" item-ids
                                                 {:prototype-f #(mongo-db/duplicated-document-prototype request :client %)
                                                  :label-key    (case name-order :reversed :client/first-name :client/last-name)})))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-items! undo-delete-items! duplicate-items! undo-duplicate-items!])

(pathom/reg-handlers! ::handlers HANDLERS)
