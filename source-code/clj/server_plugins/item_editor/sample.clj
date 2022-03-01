
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.sample
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.string :as string]
              [mongo-db.api      :as mongo-db]
              [pathom.api        :as pathom]
              [x.server-core.api :as a]
              [x.server-db.api   :as db]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [server-plugins.item-editor.api        :as item-editor]))



;; -- A plugin használatához szükséges resolver függvények --------------------
;; ----------------------------------------------------------------------------

(defn- get-item-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  [env _]
  (let [item-id (pathom/env->param env :item-id)]
       (mongo-db/get-document-by-id "my-collection" item-id)))

(defresolver get-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;  {:item-id (string)}
             ;
             ; @return (namespaced map)
             ;  {:my-extension.my-type-editor/get-item (namespaced map)}
             [env resolver-props]
             {:my-extension.my-type-editor/get-item (get-item-f env resolver-props)})



;; -- A plugin használatához szükséges mutation függvények --------------------
;; ----------------------------------------------------------------------------

; Sikeres törlés esetén a kitörölt elem azonosítójával szükséges visszatérni!
(defmutation delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [env {:keys [item-id]}]
             {::pathom.co/op-name 'my-extension.my-type-editor/delete-item!}
             (return ""))

; Sikeres visszavonás esetén a visszaállított dokumentummal szükséges visszatérni!
(defmutation undo-delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env {:keys [item]}]
             {::pathom.co/op-name 'my-extension.my-type-editor/undo-delete-item!}
             (return {}))

; Sikeres mentés esetén a dokumentummal szükséges visszatérni!
(defmutation save-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env {:keys [item]}]
             {::pathom.co/op-name 'my-extension.my-type-editor/save-item!}
             (return {}))

; - Az item-editor plugin az elem AKTUÁLIS (nem feltétlenül az elmentett) változatát küldi el a szerver számára.
; - Sikeres duplikálás esetén a létrehozott dokumentummal szükséges visszatérni!
(defmutation duplicate-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env {:keys [item]}]
             {::pathom.co/op-name 'my-extension.my-type-editor/duplicate-item!}
             (return {}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-item! duplicate-item! get-item save-item! undo-delete-item!])

(pathom/reg-handlers! ::handlers HANDLERS)



;; -- A plugin beállítása alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

; - Az [:item-editor/init-editor! ...] esemény hozzáadja a "/@app-home/my-extension/:my-type-id"
;   útvonalat a rendszerhez, amely útvonal használatával betöltődik a kliens-oldalon
;   az item-editor plugin.
; - A {:routed? false} beállítás használatával nem adja hozzá az útvonalat.
(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :my-extension :my-type]})



;; -- A plugin beállítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :my-extension :my-type
                                              {:suggestion-keys [:city :address]}]})
