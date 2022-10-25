
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.sample
    (:require [engines.file-editor.api]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [io.api                                :as io]
              [mid-fruits.candy                      :refer [return]]
              [re-frame.api                          :as r]))



;; -- Az engine beállítása ----------------------------------------------------
;; ----------------------------------------------------------------------------

; Az engine használatához SZÜKSÉGES megadni ...
; ... a {:handler-key ...} tulajdonságot, amit az engine a mutation és resolver függvények neveiben
;     névtérként használ.
;
; Az engine használatához OPCIONÁLISAN megadható ...
; ... a {:base-route "..."} tulajdonság, ami alapján az engine regisztrálja a példa szerinti
;     "/@app-home/my-editor" útvonalat.
; ... az {:on-route ...} tulajdonság, ami az engine által a példában regisztrált útvonalak használatakor
;     történik meg.
; ... a {:route-title ...} tulajdonság, ami az engine által a példában regisztrált útvonalak használatakor
;     beállítódik az applikáció címkéjének.
(r/reg-event-fx :init-my-editor!
  [:file-editor/init-editor! :my-editor
                             {:base-route  "/@app-home/my-editor"
                              :handler-key :my-handler
                              :on-route    [:my-event]
                              :route-title "My editor"}])



;; -- Az engine használatához szükséges resolver függvények --------------------
;; ----------------------------------------------------------------------------

(defn- get-content-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  [env _]
  (io/read-edn-file "my-file.edn"))

(defresolver get-content
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:my-handler/get-content (map)}
             [env resolver-props]
             {:my-handler/get-content (get-content-f env resolver-props)})



;; -- Az engine használatához szükséges mutation függvények --------------------
;; ----------------------------------------------------------------------------

; Sikeres mentés esetén a tartalommal szükséges visszatérni!
(defmutation save-content!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:content (map)}
             ;
             ; @return (map)
             [env {:keys [content]}]
             {::pathom.co/op-name 'my-handler/save-content!}
             (io/write-edn-file! "my-file.edn" content)
             (return content))
