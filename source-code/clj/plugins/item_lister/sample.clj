
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.sample
    (:require [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [mid-fruits.candy                      :refer [param return]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [plugins.item-lister.api               :as item-lister]
              [x.server-core.api                     :as a]))



;; -- A plugin használatához szükséges resolver függvények --------------------
;; ----------------------------------------------------------------------------

; - :documents
;   A keresési feltételeknek megfelelő dokumentumok rendezve, skip-elve és limit-elve
; - :document-count
;   A keresési feltételeknek megfelelő dokumentumok száma
(defn- get-items-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:document-count (integer)
  ;   :documents (namespaced maps in vector)}}
  [env _]
  (let [get-pipeline   (item-lister/env->get-pipeline   env :my-lister)
        count-pipeline (item-lister/env->count-pipeline env :my-lister)]
       {:documents      (mongo-db/get-documents-by-pipeline   "my-collection" get-pipeline)
        :document-count (mongo-db/count-documents-by-pipeline "my-collection" count-pipeline)}))

(defresolver get-items
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:my-handler/get-items (map)
             ;    {:document-count (integer)
             ;     :documents (namespaced maps in vector)}}
             [env resolver-props]
             {:my-handler/get-items (get-items-f env resolver-props)})



;; -- A plugin használatához szükséges mutation függvények --------------------
;; ----------------------------------------------------------------------------

; Sikeres törlés esetén a kitörölt elemek azonosítóival szükséges visszatérni!
(defmutation delete-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (strings in vector)
             [env {:keys [item-ids]}]
             {::pathom.co/op-name 'my-handler/delete-items!}
             (return []))

; Sikeres visszavonás esetén a visszaállított dokumentumokkal szükséges visszatérni!
(defmutation undo-delete-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:items (namespaced maps in vector)}
             ;
             ; @return (namespaced maps in vector)
             [env {:keys [items]}]
             {::pathom.co/op-name 'my-handler/undo-delete-items!}
             (return []))

; Sikeres duplikálás esetén a létrehozott dokumentumokkal szükséges visszatérni!
(defmutation duplicate-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (namespaced maps in vector)
             [env {:keys [item-ids]}]
             {::pathom.co/op-name 'my-handler/duplicate-items!}
             (return []))

; Sikeres visszavonás esetén a kitörölt elemek azonosítóival szükséges visszatérni!
(defmutation undo-duplicate-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (strings in vector)
             [env {:keys [item-ids]}]
             {::pathom.co/op-name 'my-handler/undo-duplicate-items!}
             (return []))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-items! duplicate-items! get-items undo-delete-items! undo-duplicate-items!])

(pathom/reg-handlers! ::handlers HANDLERS)
