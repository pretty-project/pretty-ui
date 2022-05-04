
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.mutations
    (:require [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [mid-fruits.vector                     :as vector]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation undo-delete-items!
             [_ {:keys [items]}]
             {::pathom.co/op-name 'clients.client-lister/undo-delete-items!}
             ; XXX#7601
             ; A :client/name virtuális mezőt szükséges eltávolítani a dokumentumokból!
             (letfn [(f [item] (dissoc item :client/name))]
                    (mongo-db/insert-documents! "clients" (vector/->items items f))))

(defmutation delete-items!
             [_ {:keys [item-ids]}]
             {::pathom.co/op-name 'clients.client-lister/delete-items!}
             (mongo-db/remove-documents! "clients" item-ids))

(defmutation duplicate-items!
             [{:keys [request]} {:keys [item-ids]}]
             {::pathom.co/op-name 'clients.client-lister/duplicate-items!}
             (mongo-db/duplicate-documents! "clients" item-ids
                                            {:prototype-f #(mongo-db/duplicated-document-prototype request :client %)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def HANDLERS [delete-items! undo-delete-items! duplicate-items!])

(pathom/reg-handlers! ::handlers HANDLERS)
