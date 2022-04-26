
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
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [_ {:keys [items]}]
             {::pathom.co/op-name 'clients.client-lister/undo-delete-items!}
             ; XXX#7601
             ; A :client/name virtuális mezőt szükséges eltávolítani a dokumentumokból!
             (letfn [(f [item] (dissoc item :client/name))]
                    (mongo-db/insert-documents! "clients" (vector/->items items f))))

(defmutation delete-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [_ {:keys [item-ids]}]
             {::pathom.co/op-name 'clients.client-lister/delete-items!}
             (mongo-db/remove-documents! "clients" item-ids))

(defmutation duplicate-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [{:keys [request]} {:keys [item-ids]}]
             {::pathom.co/op-name 'clients.client-lister/duplicate-items!}
             (mongo-db/duplicate-documents! "clients" item-ids
                                            {:prototype-f #(mongo-db/duplicated-document-prototype request :client %)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-items! undo-delete-items! duplicate-items!])

(pathom/reg-handlers! ::handlers HANDLERS)
