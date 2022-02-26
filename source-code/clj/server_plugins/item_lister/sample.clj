
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.sample
    (:require [mid-fruits.candy  :refer [param return]]
              [mongo-db.api      :as mongo-db]
              [pathom.api        :as pathom]
              [x.server-core.api :as a]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [server-plugins.item-lister.api        :as item-lister]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced keywords in vector)
(def ORDER-BY-OPTIONS [:modified-at/descending :modified-at/ascending :name/ascending :name/descending])



;; -- A plugin használatához szükséges resolver függvények --------------------
;; ----------------------------------------------------------------------------

(defn- get-my-type-items-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:document-count (integer)
  ;   :documents (namespaced maps in vector)}}
  [env _]
  (let [get-pipeline   (item-lister/env->get-pipeline   env :my-extension :my-type)
        count-pipeline (item-lister/env->count-pipeline env :my-extension :my-type)]
        ; A keresési feltételeknek megfelelő dokumentumok rendezve, skip-elve és limit-elve
       {:documents      (mongo-db/get-documents-by-pipeline   "my-collection" get-pipeline)
        ; A keresési feltételeknek megfelelő dokumentumok száma
        :document-count (mongo-db/count-documents-by-pipeline "my-collection" count-pipeline)}))

(defresolver get-my-type-items
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:my-extension/get-my-type-items (map)
             ;    {:document-count (integer)
             ;     :documents (namespaced maps in vector)}}
             [env resolver-props]
             {:my-extension/get-my-type-items (get-my-type-items-f env resolver-props)})



;; -- A plugin használatához szükséges mutation függvények --------------------
;; ----------------------------------------------------------------------------

(defmutation undo-delete-my-type-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:items (namespaced maps in vector)}
             ;
             ; @return (namespaced maps in vector)
             [env {:keys [items]}]
             {::pathom.co/op-name 'my-extension/undo-delete-my-type-items!}
             (return []))

(defmutation delete-my-type-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (strings in vector)
             [env {:keys [item-ids]}]
             {::pathom.co/op-name 'my-extension/delete-my-type-items!}
             (return []))

(defmutation undo-duplicate-my-type-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (strings in vector)
             [env {:keys [item-ids]}]
             {::pathom.co/op-name 'my-extension/undo-duplicate-my-type-items!}
             (return []))

(defmutation duplicate-my-type-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (namespaced maps in vector)
             [env {:keys [item-ids]}]
             {::pathom.co/op-name 'my-extension/duplicate-my-type-items!}
             (return []))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-my-type-items delete-my-type-items!    undo-delete-my-type-items!
                                 duplicate-my-type-items! undo-duplicate-my-type-items!])

(pathom/reg-handlers! ::handlers HANDLERS)



;; -- A plugin beállítása alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

; - Az [:item-lister/init-lister! ...] esemény hozzáadja a "/@app-home/my-extension" útvonalat
;   a rendszerhez, amely útvonal használatával betöltődik a kliens-oldalon az item-lister plugin.
; - A {:routed? false} beállítás használatával nem adja hozzá az útvonalat.
(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :my-extension :my-type]})



;; -- A plugin beállítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

; A dokumentumoknak tartalmazniuk kell legalább egyet a {:search-keys [...]} tulajdonságként
; átadott vektorban felsorolt kulcsok közül!
(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :my-extension :my-type
                                              {:download-limit 10
                                               :order-by-options ORDER-BY-OPTIONS
                                               :search-keys [:my-key]}]})
