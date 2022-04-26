
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.sample
    (:require [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [mid-fruits.candy                      :refer [param return]]
              [mid-fruits.string                     :as string]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [x.server-core.api                     :as a]
              [x.server-db.api                       :as db]))



;; -- A plugin beállítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

; A plugin használatához SZÜKSÉGES megadni ...
; ... a {:collection-name "..."} tulajdonságot.
; ... a {:handler-key ...} tulajdonságot, amit a plugin a mutation és resolver függvények neveiben
;     névtérként használ.
; ... az {:item-namespace ...} tulajdonságot.
;
; A plugin használatához OPCIONÁLISAN megadható ...
; ... a {:base-route "..."} tulajdonság, ami alapján a plugin regisztrálja a példa szerinti
;     "/@app-home/my-editor/:item-id/:view-id" útvonalat.
; ... az {:on-route ...} tulajdonság, ami a plugin által a példában regisztrált
;     "/@app-home/my-editor/:item-id/:view-id" útvonal használatakor történik meg.
; ... a {:route-title ...} tulajdonság, ami a plugin által a példában regisztrált
;     "/@app-home/my-editor/:item-id/:view-id" útvonal használatakor beállítódik az applikáció címkéjének.
(a/reg-event-fx
  :init-my-editor!
  [:item-editor/init-editor! :my-editor
                             {:base-route      "/@app-home/my-editor"
                              :collection-name "my-collection"
                              :handler-key     :my-handler
                              :item-namespace  :my-type
                              :on-route        [:my-event]
                              :route-title     "My editor"}])



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
             ;  {:my-handler/get-item (namespaced map)}
             [env resolver-props]
             {:my-handler/get-item (get-item-f env resolver-props)})



;; -- A plugin használatához szükséges mutation függvények --------------------
;; ----------------------------------------------------------------------------

; Sikeres létrehozás esetén a dokumentummal szükséges visszatérni!
(defmutation add-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env {:keys [item]}]
             {::pathom.co/op-name 'my-handler/add-item!}
             (return {}))

; Sikeres törlés esetén a kitörölt elem azonosítójával szükséges visszatérni!
(defmutation delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [env {:keys [item-id]}]
             {::pathom.co/op-name 'my-handler/delete-item!}
             (return ""))

; Sikeres visszavonás esetén a visszaállított dokumentummal szükséges visszatérni!
(defmutation undo-delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env {:keys [item]}]
             {::pathom.co/op-name 'my-handler/undo-delete-item!}
             (return {}))

; Sikeres mentés esetén a dokumentummal szükséges visszatérni!
(defmutation save-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env {:keys [item]}]
             {::pathom.co/op-name 'my-handler/save-item!}
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
             {::pathom.co/op-name 'my-handler/duplicate-item!}
             (return {}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-item! duplicate-item! get-item save-item! undo-delete-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
