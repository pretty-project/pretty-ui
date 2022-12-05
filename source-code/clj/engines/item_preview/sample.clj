
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-preview.sample
    (:require [engines.item-editor.api]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [re-frame.api                          :as r]))



;; -- Az engine beállítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

; Az engine használatához SZÜKSÉGES megadni ...
; ... a {:collection-name "..."} tulajdonságot, amit az engine jelenleg nem használ (x4.7.5).
; ... a {:handler-key ...} tulajdonságot, amit az engine a resolver függvények neveiben
;    névtérként használ.
; ... az {:item-namespace ...} tulajdonságot.
(r/reg-event-fx :init-my-preview!
  [:item-preview/init-preview! :my-preview
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
             ;  {:item-id (string)}
             ;
             ; @return (namespaced map)
             ;  {:my-handler/get-item (namespaced map)}
             [env resolver-props]
             {:my-handler/get-item (get-item-f env resolver-props)})
