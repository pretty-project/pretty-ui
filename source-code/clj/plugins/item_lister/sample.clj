
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.sample
    (:require [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [mid-fruits.candy                      :refer [return]]
              [mongo-db.api                          :as mongo-db]
              [plugins.item-lister.api               :as item-lister]
              [x.server-core.api                     :as a]))



;; -- A plugin beállítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

; A plugin használatához SZÜKSÉGES megadni ...
; ... a {:collection-name "..."} tulajdonságot, amit a plugin jelenleg nem használ (x4.7.5).
; ... a {:handler-key ...} tulajdonságot, amit a plugin a mutation és resolver függvények neveiben
;     névtérként használ.
; ... az {:item-namespace ...} tulajdonságot.
;
; A plugin használatához OPCIONÁLISAN megadható ...
; ... az {:on-route ...} tulajdonság, ami a plugin által a példában regisztrált "/@app-home/my-lister"
;     útvonal használatakor történik meg.
; ... a {:base-route "..."} tulajdonságot, ami alapján a plugin regisztrálja a példa szerinti útvonalat.
; ... a {:route-title ...} tulajdonságot, ami a plugin által a példában regisztrált útvonalak
;     használatakor beállítódik az applikáció címkéjének.
(a/reg-event-fx
  :init-my-lister!
  [:item-lister/init-lister! :my-lister
                             {:base-route      "/@app-home/my-lister"
                              :collection-name "my_collection"
                              :handler-key     :my-handler
                              :item-namespace  :my-type
                              :on-route        [:my-event]
                              :route-title     "My lister"}])



;; -- A plugin használatához szükséges resolver függvények --------------------
;; ----------------------------------------------------------------------------

; - :documents
;   A keresési feltételeknek megfelelő dokumentumok rendezve, skip-elve és limit-elve
;
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
       {:documents      (mongo-db/get-documents-by-pipeline   "my_collection" get-pipeline)
        :document-count (mongo-db/count-documents-by-pipeline "my_collection" count-pipeline)}))

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
