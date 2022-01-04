
(ns server-plugins.item-editor.sample
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.string    :as string]
              [mid-fruits.validator :as validator]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [x.server-core.api    :as a]
              [x.server-db.api      :as db]
              [server-plugins.item-editor.api        :as item-editor]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defresolver get-my-type-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;  {:my-type/id (string)}
             ;
             ; @return (map)
             ;  {:my-extension/get-my-type-item (map)}
             [env {:keys [my-type/id]}]
             ; A felsorolt tulajdonságok szükségesek az item-editor plugin működéséhez:
             {::pco/output [:my-type/id
                            :my-type/added-at
                            :my-type/archived?
                            :my-type/description
                            :my-type/favorite?
                            :my-type/modified-at]}
             (if-let [document (mongo-db/get-document-by-id "my-collection" id)]
                     ; XXX#6074
                     (validator/validate-data document)))



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation undo-delete-my-type-item!
             ; @param (map) env
             ; @param (namespaced map) my-type-item
             ;
             ; @return (namespaced map)
             [env my-type-item]
             {::pco/op-name 'my-extension/undo-delete-my-type-item!}
             (return {}))

(defmutation save-my-type-item!
             ; @param (map) env
             ; @param (namespaced map) my-type-item
             ;
             ; @return (namespaced map)
             [env my-type-item]
             {::pco/op-name 'my-extension/save-my-type-item!}
             (return {}))

(defmutation merge-my-type-item!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (namespaced map) client-item
             ;
             ; @return (namespaced map)
             [env client-item]
             {::pco/op-name 'my-extension/merge-my-type-item!}
             (return {}))

(defmutation delete-my-type-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [{:keys [item-id]}]
             {::pco/op-name 'my-extension/delete-my-type-item!}
             (return ""))

(defmutation duplicate-my-type-item!
             ; @param (map) env
             ; @param (namespaced map) my-type-item
             ;
             ; @return (namespaced map)
             [env my-type-item]
             {::pco/op-name 'my-extension/duplicate-my-type-item!}
             (return {}))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-my-type-item! duplicate-my-type-item! get-my-type-item
               merge-my-type-item!  save-my-type-item!      undo-delete-my-type-item!])

(pathom/reg-handlers! :my-type-editor HANDLERS)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  ; Az [:item-editor/initialize! ...] esemény hozzáadja a "/my-extension/:my-type-id" útvonalat
  ; a rendszerhez, amely útvonal használatával betöltődik a kliens-oldalon az item-editor plugin.
  {:on-app-boot [:item-editor/initialize! :my-extension :my-type]})

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:item-editor/initialize! :my-extension :my-type
                                          {:handle-archived-items? false
                                           :handle-favorite-items? false
                                           :suggestion-keys [:city :address]}]})
