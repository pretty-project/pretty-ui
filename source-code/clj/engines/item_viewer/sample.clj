
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.sample
    (:require [engines.item-viewer.api]
              [candy.api                             :refer [return]]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [re-frame.api                          :as r]))



;; -- Az engine beállítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

; Az engine használatához SZÜKSÉGES megadni ...
; ... a {:collection-name "..."} tulajdonságot, amit az engine jelenleg nem használ (x4.7.5).
; ... a {:handler-key ...} tulajdonságot, amit az engine a mutation és resolver függvények neveiben
;     névtérként használ.
; ... az {:item-namespace ...} tulajdonságot.
;
; Az engine használatához OPCIONÁLISAN megadható ...
; ... a {:base-route "..."} tulajdonság, ami alapján az engine regisztrálja a példa szerinti
;     "/@app-home/my-viewer/:item-id" útvonalat.
; ... az {:on-route ...} tulajdonság, ami az engine által a példában regisztrált útvonalak használatakor
;     történik meg.
; ... a {:route-title ...} tulajdonság, ami az engine által a példában regisztrált útvonalak használatakor
;     beállítódik az applikáció címkéjének.
(r/reg-event-fx :init-my-viewer!
  [:item-viewer/init-viewer! :my-viewer
                             {:base-route      "/@app-home/my-viewer"
                              :collection-name "my_collection"
                              :handler-key     :my-handler
                              :item-namespace  :my-type
                              :on-route        [:my-event]
                              :route-title     "My viewer"}])



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

(defresolver get-item
             ; @param (map) env
             ; @param (map) resolver-props
             ;  {:item-id (string)}
             ;
             ; @return (namespaced map)
             ;  {:my-handler/get-item (namespaced map)}
             [env resolver-props]
             {:my-handler/get-item (get-item-f env resolver-props)})



;; -- Az engine használatához szükséges mutation függvények --------------------
;; ----------------------------------------------------------------------------

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
