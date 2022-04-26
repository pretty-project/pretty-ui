
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
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [_ {:keys [item-id]}]
             {::pathom.co/op-name 'clients.client-viewer/delete-item!}
             (mongo-db/remove-document! "clients" item-id))

(defmutation undo-delete-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [_ {:keys [item]}]
             {::pathom.co/op-name 'clients.client-viewer/undo-delete-item!}
             ; XXX#7601
             ; A :client/name virtális mezőt szükséges eltávolítani a dokumentumokból!
             (mongo-db/insert-document! "clients" (dissoc item :client/name)))

(defmutation duplicate-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [{:keys [request] :as env} {:keys [item]}]
             {::pathom.co/op-name 'clients.client-viewer/duplicate-item!}
             ; XXX#7601
             (mongo-db/duplicate-document! "clients" (:client/id item)
                                           {:changes      (dissoc item :client/name)
                                            :prototype-f #(mongo-db/duplicated-document-prototype request :client %)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-item! duplicate-item! undo-delete-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
