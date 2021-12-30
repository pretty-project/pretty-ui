
(ns server-extensions.clients.client-editor
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.validator :as validator]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [x.server-core.api    :as a]
              [x.server-db.api      :as db]
              [server-plugins.item-editor.api :as item-editor]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defresolver get-client-item
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;  {:client/id (string)}
             ;
             ; @return (map)
             ;  {:clients/get-client-item (map)}
             [env {:keys [client/id]}]
             {::pco/output [:client/id
                            :client/added-at
                            :client/address
                            :client/archived?
                            :client/city
                            :client/colors
                            :client/country
                            :client/description
                            :client/email-address
                            :client/favorite?
                            :client/first-name
                            :client/last-name
                            :client/modified-at
                            :client/phone-number
                            :client/vat-no
                            :client/zip-code]}
             (if-let [document (mongo-db/get-document-by-id "clients" id)]
                     (validator/validate-data document)))



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
             {::pco/op-name 'clients/undo-delete-client-item!}
             (mongo-db/add-document! "clients" client-item))

(defmutation save-client-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) client-item
             ;
             ; @return (namespaced map)
             [env client-item]
             {::pco/op-name 'clients/save-client-item!}
             (mongo-db/upsert-document! "clients" client-item
                                        {:prototype-f #(item-editor/updated-item-prototype env :clients :client %)}))

(defmutation merge-client-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) client-item
             ;
             ; @return (namespaced map)
             [env client-item]
             {::pco/op-name 'clients/merge-client-item!}
             (mongo-db/merge-document! "clients" client-item
                                       {:prototype-f #(item-editor/updated-item-prototype env :clients :client %)}))

(defmutation delete-client-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [{:keys [item-id]}]
             {::pco/op-name 'clients/delete-client-item!}
             (mongo-db/remove-document! "clients" item-id))

(defmutation duplicate-client-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) client-item
             ;
             ; @return (namespaced map)
             [env client-item]
             {::pco/op-name 'clients/duplicate-client-item!}
             (mongo-db/add-document! "clients" client-item
                                     {:prototype-f #(item-editor/duplicated-item-prototype env :clients :client %)}))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
(def HANDLERS [get-client-item
               undo-delete-client-item!
               save-client-item!
               merge-client-item!
               delete-client-item!
               duplicate-client-item!])

(pathom/reg-handlers! :client-editor HANDLERS)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:item-editor/initialize! :clients :client {:suggestion-keys [:city]}]})
