
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-editor.mutations
    (:require [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation add-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [{:keys [request]} {:keys [item]}]
             {::pathom.co/op-name 'clients.client-editor/add-item!}
             (mongo-db/save-document! "clients" item
                                      {:prototype-f #(mongo-db/added-document-prototype request :client %)}))

(defmutation save-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [{:keys [request]} {:keys [item]}]
             {::pathom.co/op-name 'clients.client-editor/save-item!}
             ; XXX#7601
             (mongo-db/save-document! "clients"      (dissoc item :client/name)
                                      {:prototype-f #(mongo-db/updated-document-prototype request :client %)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [add-item! save-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
