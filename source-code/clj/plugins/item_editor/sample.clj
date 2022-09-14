
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.sample
    (:require [plugins.item-editor.api]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [mid-fruits.candy                      :refer [return]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [x.server-core.api                     :as a]))



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
;     "/@app-home/my-editor/:item-id/edit" útvonalat.
; ... az {:on-route ...} tulajdonság, ami a plugin által a példában regisztrált útvonalak használatakor
;     történik meg.
; ... a {:route-title ...} tulajdonság, ami a plugin által a példában regisztrált útvonalak használatakor
;     beállítódik az applikáció címkéjének.
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
