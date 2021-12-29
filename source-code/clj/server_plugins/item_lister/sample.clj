
(ns server-plugins.item-lister.sample
    (:require [mid-fruits.candy  :refer [param return]]
              [mongo-db.api      :as mongo-db]
              [pathom.api        :as pathom]
              [x.server-core.api :as a]
              [server-plugins.item-lister.api        :as item-lister]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def ORDER-BY :by-price-ascending)

; @constant (keywords in vector)
(def ORDER-BY-OPTIONS [:by-name-ascending :by-name-descending :by-price-ascending :by-price-descending])



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defresolver get-my-type-items
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:my-extension/get-my-type-items (map)
             ;    {:document-count (integer)
             ;     :documents (maps in vector)}}
             [env _]
             {:my-extension/get-my-type-items
              (let [get-pipeline   (item-lister/env->get-pipeline   env :my-extension :my-type)
                    count-pipeline (item-lister/env->count-pipeline env :my-extension :my-type)]
                    ; A keresési feltételeknek megfelelő dokumentumok rendezve, skip-elve és limit-elve
                   {:documents      (mongo-db/get-documents-by-pipeline   :my-collection get-pipeline)
                    ; A keresési feltételeknek megfelelő dokumentumok száma
                    :document-count (mongo-db/count-documents-by-pipeline :my-collection count-pipeline)})})



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation undo-delete-my-type-items!
             ; @param (map) env
             ; @param (namespaced maps in vector) my-type-items
             ;
             ; @return (namespaced maps in vector)
             [env my-type-items]
             {::pco/op-name 'my-extension/undo-delete-my-type-items!}
             (return []))

(defmutation delete-my-type-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (strings in vector)
             [{:keys [item-ids]}]
             {::pco/op-name 'my-extension/delete-my-type-items!}
             (return []))



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
(def HANDLERS [get-my-type-items
               undo-delete-my-type-items!
               delete-my-type-items!])

(pathom/reg-handlers! :my-extension HANDLERS)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  ; Az [:item-lister/initialize! ...] esemény hozzáadja a "/my-extension" útvonalat a rendszerhez,
  ; amely útvonal használatával betöltődik a kliens-oldalon az item-lister plugin.
  {:on-app-boot [:item-lister/initialize! :my-extension :my-type]})

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:item-lister/initialize! :my-extension :my-type
                                          {:download-limit         10
                                           :order-by         ORDER-BY
                                           :order-by-options ORDER-BY-OPTIONS
                                           :search-keys [:my-key :your-key]}]})
