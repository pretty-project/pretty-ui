
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.clients.client-editor.mutations
    (:require [mid-fruits.candy :refer [param return]]
              [mongo-db.api     :as mongo-db]
              [pathom.api       :as pathom]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]))



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation undo-delete-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [_ {:keys [item]}]
             {::pathom.co/op-name 'clients.client-editor/undo-delete-item!}
             (mongo-db/insert-document! "clients" item))

(defmutation save-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [{:keys [request]} {:keys [item]}]
             {::pathom.co/op-name 'clients.client-editor/save-item!}
             (mongo-db/save-document! "clients" item
                                      {:prototype-f #(mongo-db/updated-document-prototype request :client %)}))

(defmutation delete-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [_ {:keys [item-id]}]
             {::pathom.co/op-name 'clients.client-editor/delete-item!}
             (mongo-db/remove-document! "clients" item-id))

(defmutation duplicate-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [{:keys [request] :as env} {:keys [item]}]
             {::pathom.co/op-name 'clients.client-editor/duplicate-item!}
             (mongo-db/duplicate-document! "clients" (:id item)
                                           {:changes item
                                            :prototype-f #(mongo-db/duplicated-document-prototype request :client %)}))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-item! duplicate-item! save-item! undo-delete-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
