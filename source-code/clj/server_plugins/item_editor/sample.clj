
(ns server-plugins.item-editor.sample
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.string    :as string]
              [mid-fruits.validator :as validator]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [x.server-core.api    :as a]
              [x.server-db.api      :as db]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [server-plugins.item-editor.api        :as item-editor]))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-my-type-item-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  [env _]
  (let [item-id (pathom/env->param env :item-id)]
       (if-let [document (mongo-db/get-document-by-id "my-collection" item-id)]
               ; XXX#6074
               (validator/validate-data document))))

(defresolver get-my-type-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;  {:my-type/id (string)}
             ;
             ; @return (namespaced map)
             ;  {:my-extension/get-my-type-item (namespaced map)}
             [env resolver-props]
             {:my-extension/get-my-type-item (get-my-type-item-f env resolver-props)})



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation undo-delete-my-type-item!
             ; @param (map) env
             ; @param (namespaced map) my-type-item
             ;
             ; @return (namespaced map)
             [env my-type-item]
             {::pathom.co/op-name 'my-extension/undo-delete-my-type-item!}
             (return {}))

(defmutation save-my-type-item!
             ; @param (map) env
             ; @param (namespaced map) my-type-item
             ;
             ; @return (namespaced map)
             [env my-type-item]
             {::pathom.co/op-name 'my-extension/save-my-type-item!}
             (return {}))

(defmutation delete-my-type-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [env {:keys [item-id]}]
             {::pathom.co/op-name 'my-extension/delete-my-type-item!}
             (return ""))

(defmutation duplicate-my-type-item!
             ; @param (map) env
             ; @param (namespaced map) my-type-item
             ;
             ; @return (namespaced map)
             [env my-type-item]
             {::pathom.co/op-name 'my-extension/duplicate-my-type-item!}
             ; Az item-editor plugin az elem aktuális (nem feltétlenül az elmentett) változatát
             ; küldi el a szerver számára, hogy az új elemként hozza létre az elküldött másolatot.
             (return {}))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-my-type-item! duplicate-my-type-item! get-my-type-item
               save-my-type-item!   undo-delete-my-type-item!])

(pathom/reg-handlers! ::handlers HANDLERS)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  ; Az [:item-editor/initialize! ...] esemény hozzáadja a "/@app-home/my-extension/:my-type-id"
  ; útvonalat a rendszerhez, amely útvonal használatával betöltődik a kliens-oldalon
  ; az item-editor plugin.
  {:on-server-boot [:item-editor/initialize! :my-extension :my-type]})

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot [:item-editor/initialize! :my-extension :my-type
                                             {:suggestion-keys [:city :address]}]})
