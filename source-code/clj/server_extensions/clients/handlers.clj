
(ns server-extensions.clients.handlers
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.validator :as validator]
              [mid-fruits.vector    :as vector]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [x.server-core.api    :as a]
              [x.server-db.api      :as db]
              [x.server-locales.api :as locales]
              [x.server-user.api    :as user]
              [server-plugins.item-editor.api        :as item-editor]
              [server-plugins.item-lister.api        :as item-lister]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def COLLECTION-NAME "clients")



;; -- Pipelines ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- env->name-field-operation
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env]
  (let [request           (pathom/env->request env)
        selected-language (user/request->user-settings-item request :selected-language)
        name-order        (get locales/NAME-ORDERS selected-language)]
       ; - A pipeline elejére fűz egy field-operation műveletet, amivel hozzáadja a :client/name
       ;   tulajdonságot a dokumentumokhoz.
       ; - A :client/first-name és :client/last-name tulajdonságok sorrendjéhez a felhasználó által
       ;   kiválaszott nyelv szerinti sorrendet alkalmazza.
       (case name-order :reversed (mongo-db/field-pattern->field-operation [:client/name [:client/last-name  :client/first-name]])
                                  (mongo-db/field-pattern->field-operation [:client/name [:client/first-name :client/last-name]]))))

(defn- env->get-pipeline
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env]
  (let [name-field-operation (env->name-field-operation     env)
        get-pipeline         (item-lister/env->get-pipeline env :clients :client)]
       (vector/cons-item get-pipeline name-field-operation)))

(defn- env->count-pipeline
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env]
  (let [name-field-operation (env->name-field-operation  env)
        count-pipeline  (item-lister/env->count-pipeline env :clients :client)]
       (vector/cons-item count-pipeline name-field-operation)))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defresolver get-client-items
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:clients/get-client-items (map)
             ;    {:document-count (integer)
             ;     :documents (maps in vector)}}
             [env _]
             {:clients/get-client-items
              (let [get-pipeline   (env->get-pipeline   env)
                    count-pipeline (env->count-pipeline env)]
                   {:documents      (mongo-db/get-documents-by-pipeline   COLLECTION-NAME get-pipeline)
                    :document-count (mongo-db/count-documents-by-pipeline COLLECTION-NAME count-pipeline)})})

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
             (if-let [document (mongo-db/get-document-by-id COLLECTION-NAME id)]
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
             (mongo-db/add-document! COLLECTION-NAME client-item))

(defmutation undo-delete-client-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) mutation-props
             ;  {:items (namespaced maps in vector)}
             ;
             ; @return (namespaced maps in vector)
             [{:keys [items]}]
             {::pco/op-name 'clients/undo-delete-client-items!}
             (mongo-db/add-documents! COLLECTION-NAME items))

(defmutation save-client-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) client-item
             ;
             ; @return (namespaced map)
             [env client-item]
             {::pco/op-name 'clients/save-client-item!}
             (mongo-db/upsert-document! COLLECTION-NAME client-item
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
             (mongo-db/merge-document! COLLECTION-NAME client-item
                                       {:prototype-f #(item-editor/updated-item-prototype env :clients :client %)}))

(defmutation merge-client-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:items (namespaced maps in vector)}
             ;
             ; @return (namespaced maps in vector)
             [env {:keys [items]}]
             {::pco/op-name 'clients/merge-client-items!}
             (mongo-db/merge-documents! COLLECTION-NAME items
                                        {:prototype-f #(item-lister/updated-item-prototype env :clients :client %)}))

(defmutation delete-client-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [{:keys [item-id]}]
             {::pco/op-name 'clients/delete-client-item!}
             (mongo-db/remove-document! COLLECTION-NAME item-id))

(defmutation delete-client-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (strings in vector)
             [{:keys [item-ids]}]
             {::pco/op-name 'clients/delete-client-items!}
             (mongo-db/remove-documents! COLLECTION-NAME item-ids))

(defmutation duplicate-client-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) client-item
             ;
             ; @return (namespaced map)
             [env client-item]
             {::pco/op-name 'clients/duplicate-client-item!}
             (mongo-db/add-document! COLLECTION-NAME client-item
                                     {:prototype-f #(item-editor/duplicated-item-prototype env :clients :client %)}))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
(def HANDLERS [get-client-items
               get-client-item
               undo-delete-client-item!
               undo-delete-client-items!
               save-client-item!
               merge-client-item!
               merge-client-items!
               delete-client-item!
               delete-client-items!
               duplicate-client-item!])

(pathom/reg-handlers! :clients HANDLERS)
