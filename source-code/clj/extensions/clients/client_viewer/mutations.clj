
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-viewer.mutations
    (:require [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [mid-fruits.candy                      :refer [param return]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation delete-item!
             [_ {:keys [item-id]}]
             {::pathom.co/op-name 'clients.client-viewer/delete-item!}
             (mongo-db/remove-document! "clients" item-id))

(defmutation undo-delete-item!
             [_ {:keys [item]}]
             {::pathom.co/op-name 'clients.client-viewer/undo-delete-item!}
             ; XXX#7601
             ; A :client/name virtális mezőt szükséges eltávolítani a dokumentumokból!
             (mongo-db/insert-document! "clients" (dissoc item :client/name)))

(defmutation duplicate-item!
             [{:keys [request] :as env} {:keys [item-id]}]
             {::pathom.co/op-name 'clients.client-viewer/duplicate-item!}
             (mongo-db/duplicate-document! "clients" item-id
                                           {:prototype-f #(mongo-db/duplicated-document-prototype request :client %)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def HANDLERS [delete-item! duplicate-item! undo-delete-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
