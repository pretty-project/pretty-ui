
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.sample
    (:require [engines.item-handler.api]
              [candy.api                             :refer [return]]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [re-frame.api                          :as r]))



;; -- Az engine beállítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

; Az engine használatához SZÜKSÉGES megadni ...
; ... a {:collection-name "..."} tulajdonságot, amit az engine az ajánlott értékek (suggestions)
;     letöltésekor használ.
; ... a {:handler-key ...} tulajdonságot, amit az engine a mutation és resolver függvények neveiben
;     névtérként használ.
; ... az {:item-namespace ...} tulajdonságot.
;
; Az engine használatához OPCIONÁLISAN megadható ...
; ...
(r/reg-event-fx :init-my-handler!
  [:item-handler/init-handler! :my-handler
                               {:collection-name "my_collection"
                                :handler-key     :my-handler
                                :item-namespace  :my-type}])



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
             ; {:item-id (string)}
             ;
             ; @return (namespaced map)
             ; {:my-handler/get-item (namespaced map)}
             [env resolver-props]
             {:my-handler/get-item (get-item-f env resolver-props)})



;; -- Az engine használatához szükséges mutation függvények --------------------
;; ----------------------------------------------------------------------------

; Sikeres létrehozás esetén a dokumentummal szükséges visszatérni!
(defmutation add-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ; {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env {:keys [item]}]
             {::pathom.co/op-name 'my-handler/add-item!}
             (return {}))

; Sikeres törlés esetén a dokumentum azonosítójával szükséges visszatérni!
(defmutation delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ; {:item-id (string)}
             ;
             ; @return (string)
             [env {:keys [item-id]}]
             {::pathom.co/op-name 'my-handler/delete-item!}
             (return item-id))

; Sikeres duplikálás esetén a dokumentum azonosítójával szükséges visszatérni!
(defmutation duplicate-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ; {:item-id (string)}
             ;
             ; @return (string)
             [env {:keys [item-id]}]
             {::pathom.co/op-name 'my-handler/duplicate-item!}
             (return item-id))

; Sikeres mentés esetén a dokumentummal szükséges visszatérni!
(defmutation save-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ; {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env {:keys [item]}]
             {::pathom.co/op-name 'my-handler/save-item!}
             (return {}))

; Sikeres visszavonás esetén a visszaállított dokumentummal szükséges visszatérni!
(defmutation undo-delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ; {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env {:keys [item]}]
             {::pathom.co/op-name 'my-handler/undo-delete-item!}
             (return {}))
