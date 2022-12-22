
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.sample
    (:require [candy.api                             :refer [return]]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [engines.item-lister.api               :as item-lister]
              [mongo-db.api                          :as mongo-db]
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
; ...
(r/reg-event-fx :init-my-lister!
  [:item-lister/init-lister! :my-lister
                             {:collection-name "my_collection"
                              :handler-key     :my-handler
                              :item-namespace  :my-type}])



;; -- Az engine használatához szükséges resolver függvények --------------------
;; ----------------------------------------------------------------------------

; :items
; A keresési feltételeknek megfelelő dokumentumok rendezve, skip-elve és limit-elve
;
; :all-item-count
; A keresési feltételeknek megfelelő dokumentumok száma
(defn- get-items-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ; {:document-count (integer)
  ;  :documents (namespaced maps in vector)}}
  [env _]
  (let [get-pipeline   (item-lister/env->get-pipeline   env :my-lister)
        count-pipeline (item-lister/env->count-pipeline env :my-lister)]
       {:all-item-count (mongo-db/count-documents-by-pipeline "my_collection" count-pipeline)
        :items          (mongo-db/get-documents-by-pipeline   "my_collection" get-pipeline)}))

(defresolver get-items
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ; {:my-handler/get-items (map)
             ;   {:document-count (integer)
             ;    :documents (namespaced maps in vector)}}
             [env resolver-props]
             {:my-handler/get-items (get-items-f env resolver-props)})



;; -- Az engine használatához szükséges mutation függvények --------------------
;; ----------------------------------------------------------------------------

; Sikeres átrendezés esetén az átrendezett dokumentumokkal szükséges visszatérni!
(defmutation reorder-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ; {:items (namespaced maps in vector)}
             ;
             ; @return (namespaced maps in vector)
             [env mutation-props]
             {::pathom.co/op-name 'my-handler/reorder-items!}
             (return []))

; Sikeres törlés esetén a kitörölt elemek azonosítóival szükséges visszatérni!
(defmutation delete-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ; {:item-ids (strings in vector)}
             ;
             ; @return (strings in vector)
             [env {:keys [item-ids]}]
             {::pathom.co/op-name 'my-handler/delete-items!}
             (return []))

; Sikeres visszavonás esetén a visszaállított dokumentumokkal szükséges visszatérni!
(defmutation undo-delete-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ; {:items (namespaced maps in vector)}
             ;
             ; @return (namespaced maps in vector)
             [env {:keys [items]}]
             {::pathom.co/op-name 'my-handler/undo-delete-items!}
             (return []))

; Sikeres duplikálás esetén a létrehozott dokumentumokkal szükséges visszatérni!
(defmutation duplicate-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ; {:item-ids (strings in vector)}
             ;
             ; @return (namespaced maps in vector)
             [env {:keys [item-ids]}]
             {::pathom.co/op-name 'my-handler/duplicate-items!}
             (return []))
