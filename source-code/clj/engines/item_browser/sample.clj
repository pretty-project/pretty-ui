
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.sample
    (:require [engines.item-browser.api]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [mid-fruits.candy                      :refer [return]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [re-frame.api                          :as r]))



;; -- Az engine beállítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

; Az engine használatához SZÜKSÉGES megadni ...
; ...
;
; Az engine használatához OPCIONÁLISAN megadható ...
; ...
(r/reg-event-fx :init-my-browser!
  [:item-browser/init-browser! :my-browser
                               {:collection-name "my_collection"
                                :handler-key     :my-handler
                                :on-route        [:my-event]
                                :route-template  "/@app-home/my-browser"
                                :route-title     "My browser"}])



;; -- Példa dokumentum(ok) ----------------------------------------------------
;; ----------------------------------------------------------------------------

; A böngészhető dokumentoknak tartalmazniuk kell ...
; ... a :my-type/id kulcsot!
; ... a  {:label-key ...} tulajdonságként átadott kulcsot!
; ... a  {:path-key  ...} tulajdonságként átadott kulcsot!
; ... az {:items-key ...} tulajdonságként átadott kulcsot!
(def SAMPLE-DOCUMENT {:my-type/id    "..."
                      :my-type/name  "My document"
                      :my-type/path  [{:my-type/id "..."} {:my-type/id "..."}]
                      :my-type/items [{:my-type/id "..."}]})



;; -- Az engine használatához szükséges resolver függvények --------------------
;; ----------------------------------------------------------------------------

(defn- get-item-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  [env _]
  (let [item-id (pathom/env->param env :item-id)]
       (mongo-db/get-document-by-id "my_collection" item-id)))

; Az item-browser engine az egyes elemek böngészésekor, a böngészett elem adatait is letölti ...
(defresolver get-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;  {:my-type/id (string)}
             ;
             ; @return (namespaced map)
             ;  {:my-handler/get-item (namespaced map)}
             [env resolver-props]
             {:my-handler/get-item (get-item-f env resolver-props)})

; Az item-browser és az item-lister engine használatakor a get-items resolver megegyezik,
; ezért annak dokumentációját az item-lister engine leírásában keresd!
(defresolver get-items
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:my-handler/get-items (map)
             ;    {:document-count (integer)
             ;     :documents (namespaced maps in vector)}}
             [env resolver-props]
             {:my-handler/get-items (fn [env resolver-props])})



;; -- Az engine használatához szükséges mutation függvények --------------------
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

; Sikeres törlés esetén a kitörölt elem azonosítójával szükséges visszatérni!
(defmutation delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (strings in vector)}
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

; Sikeres duplikálás esetén a létrehozott dokumentummal szükséges visszatérni!
(defmutation duplicate-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (namespaced map)
             [env {:keys [item-id]}]
             {::pathom.co/op-name 'my-handler/duplicate-item!}
             (return {}))

; Sikeres mentés esetén a dokumentummal szükséges visszatérni!
(defmutation update-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env {:keys [item]}]
             {::pathom.co/op-name 'my-handler/update-item!}
             (return {}))

; Sikeres áthelyezés esetén a dokumentummal szükséges visszatérni!
(defmutation move-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)
             ;   :? (?)}
             ;
             ; @return (namespaced map)
             [env {:keys [item-id]}]
             {::pathom.co/op-name 'my-handler/move-item!}
             (return {}))
