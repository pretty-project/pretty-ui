
(ns server-extensions.clients.client-lister
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.vector    :as vector]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [x.server-core.api    :as a]
              [x.server-db.api      :as db]
              [x.server-locales.api :as locales]
              [x.server-user.api    :as user]
              [server-plugins.item-lister.api :as item-lister]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))
              


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
                   {:documents      (mongo-db/get-documents-by-pipeline   "clients" get-pipeline)
                    :document-count (mongo-db/count-documents-by-pipeline "clients" count-pipeline)})})



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation undo-delete-client-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) mutation-props
             ;  {:items (namespaced maps in vector)}
             ;
             ; @return (namespaced maps in vector)
             [{:keys [items]}]
             {::pco/op-name 'clients/undo-delete-client-items!}
             (mongo-db/add-documents! "clients" items))

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
             (mongo-db/merge-documents! "clients" items
                                        {:prototype-f #(item-lister/updated-item-prototype env :clients :client %)}))

(defmutation delete-client-items!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (strings in vector)
             [{:keys [item-ids]}]
             {::pco/op-name 'clients/delete-client-items!}
             (mongo-db/remove-documents! "clients" item-ids))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
(def HANDLERS [get-client-items
               undo-delete-client-items!
               merge-client-items!
               delete-client-items!])

(pathom/reg-handlers! :client-lister HANDLERS)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:item-lister/initialize! :clients :client {:search-keys [:name :email-address]}]})
