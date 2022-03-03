
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-browser.sample
    (:require [mid-fruits.candy  :refer [param return]]
              [mongo-db.api      :as mongo-db]
              [pathom.api        :as pathom]
              [x.server-core.api :as a]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [server-plugins.item-browser.api       :as item-browser]))



;; -- Példa dokumentum --------------------------------------------------------
;; ----------------------------------------------------------------------------

; A böngészhető dokumentoknak tartalmazniuk kell ...
; ... a  {:label-key ...} tulajdonságként átadott kulcsot!
; ... a  {:path-key  ...} tulajdonságként átadott kulcsot!
; ... az {:items-key ...} tulajdonságként átadott kulcsot!
(def SAMPLE-DOCUMENT {:my-type/id    "..."
                      :my-type/name  "My document"
                      :my-type/path  [{:my-type/id "..."} {:my-type/id "..."}]
                      :my-type/items [{:my-type/id "..."}]})



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

; Az item-browser plugin az egyes elemek böngészésekor, a böngészett elem adatait is letölti ...
(defresolver get-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;  {:my-type/id (string)}
             ;
             ; @return (namespaced map)
             ;  {:my-extension.my-type-browser/get-item (namespaced map)}
             [env resolver-props]
             {:my-extension.my-type-browser/get-item (get-item-f env resolver-props)})

; - Az item-browser és az item-lister plugin használatakor a get-items resolver megegyezik,
;   ezért annak dokumentációját az item-lister plugin leírásában keresd!
; - Mivel az item-browser plugin nem rendelkezik saját listázóval és az item-lister pluginra épül,
;   ezért az item-browser pluginhoz szükséges get-items resolvert :my-extension.my-type-lister/get-items
;   azonosítóval szükséges létrehozni!
(defresolver get-items
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:my-extension.my-type-lister/get-items (map)
             ;    {:document-count (integer)
             ;     :documents (namespaced maps in vector)}}
             [env resolver-props]
             {:my-extension.my-type-lister/get-items (fn [env resolver-props])})



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
             {::pathom.co/op-name 'my-extension.my-type-lister/delete-items!}
             (return []))

; Sikeres visszavonás esetén a visszaállított dokumentumokkal szükséges visszatérni!
(defmutation undo-delete-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:items (namespaced maps in vector)}
             ;
             ; @return (namespaced maps in vector)
             [env {:keys [items]}]
             {::pathom.co/op-name 'my-extension.my-type-lister/undo-delete-items!}
             (return []))

; Sikeres duplikálás esetén a létrehozott dokumentumokkal szükséges visszatérni!
(defmutation duplicate-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (namespaced maps in vector)
             [env {:keys [item-ids]}]
             {::pathom.co/op-name 'my-extension.my-type-lister/duplicate-items!}
             (return []))

; Sikeres visszavonás esetén a kitörölt elemek azonosítóival szükséges visszatérni!
(defmutation undo-duplicate-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (strings in vector)
             [env {:keys [item-ids]}]
             {::pathom.co/op-name 'my-extension.my-type-lister/undo-duplicate-items!}
             (return []))

; Sikeres törlés esetén a kitörölt elem azonosítójával szükséges visszatérni!
(defmutation delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (strings in vector)}
             ;
             ; @return (string)
             [env {:keys [item-id]}]
             {::pathom.co/op-name 'my-extension.my-type-browser/delete-item!}
             (return ""))

; Sikeres visszavonás esetén a visszaállított dokumentummal szükséges visszatérni!
(defmutation undo-delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env {:keys [item]}]
             {::pathom.co/op-name 'my-extension.my-type-browser/undo-delete-item!}
             (return {}))

; Sikeres duplikálás esetén a létrehozott dokumentummal szükséges visszatérni!
(defmutation duplicate-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (namespaced map)
             [env {:keys [item-id]}]
             {::pathom.co/op-name 'my-extension.my-type-browser/duplicate-item!}
             (return {}))

; Sikeres visszavonás esetén a kitörölt elem azonosítójával szükséges visszatérni!
(defmutation undo-duplicate-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [env {:keys [item-id]}]
             {::pathom.co/op-name 'my-extension.my-type-browser/undo-duplicate-item!}
             (return ""))

; Sikeres mentés esetén a dokumentummal szükséges visszatérni!
(defmutation update-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env {:keys [item]}]
             {::pathom.co/op-name 'my-extension.my-type-browser/update-item!}
             (return {}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [])

(pathom/reg-handlers! ::handlers HANDLERS)



;; -- A plugin beállítása alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

; - Az item-browser plugin az item-lister plugint alkalmazza az elemek listázához,
;   a browser-props térképben található beállítások egy része az item-lister plugin beállításához
;   szükséges és leírásukat annak dokumentációjában találod!
; - Az [:item-browser/init-browser! ...] esemény {:routed? true} beállítással használva,
;   hozzáadja a "/@app-home/my-extension" és "/@app-home/my-extension/:my-type-id" útvonalakat
;   a rendszerhez, amely útvonalak használatával betöltődik a kliens-oldalon az item-browser plugin.
(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-browser/init-browser! :my-extension :my-type]})



;; -- A plugin beállítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

; A {:collection-name "..."} tulajdonság használatával a plugin kliens-oldali kezelője
; értesülhet a kollekció változásairól
(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-browser/init-browser! :my-extension :my-type
                                                {:items-key :items
                                                 :label-key :name
                                                 :path-key  :path
                                                 :collection-name "my-extension"
                                                 :root-item-id    "my-item"}]})
