
(ns server-extensions.clients.client-editor.mutations
    (:require [mid-fruits.candy     :refer [param return]]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [x.server-locales.api :as locales]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]))



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation undo-delete-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) client-item
             ;
             ; @return (namespaced map)
             [_ client-item]
             {::pathom.co/op-name 'clients.client-editor/undo-delete-item!}
             (mongo-db/insert-document! "clients" client-item))

(defmutation save-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) client-item
             ;
             ; @return (namespaced map)
             [{:keys [request]} client-item]
             {::pathom.co/op-name 'clients.client-editor/save-item!}
             (mongo-db/save-document! "clients" client-item
                                      {:prototype-f #(mongo-db/updated-document-prototype request :client %)}))

(defmutation delete-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [_ {:keys [item-id]}]
             {::pathom.co/op-name 'clients.client-editor/delete-item!}
             (mongo-db/remove-document! "clients" item-id))

(defmutation duplicate-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) client-item
             ;
             ; @return (namespaced map)
             [{:keys [request]} client-item]
             {::pathom.co/op-name 'clients.client-editor/duplicate-item!}
             (let [name-order (locales/request->name-order request)]
                  (mongo-db/insert-document! "clients" client-item
                                             {:prototype-f #(mongo-db/duplicated-document-prototype request :client %)

                                              ; Ez egy insert-document! művelet, szóval itt nem lehet átadni a label-key-t :()
                                              :label-key    (case name-order :reversed :client/first-name :client/last-name)})))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-item! duplicate-item! save-item! undo-delete-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
