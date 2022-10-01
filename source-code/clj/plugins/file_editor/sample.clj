
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.file-editor.sample
    (:require [plugins.file-editor.api]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [mid-fruits.candy                      :refer [return]]
              [server-fruits.io                      :as io]
              [re-frame.api                          :as r]))



;; -- A plugin beállítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

; A plugin használatához SZÜKSÉGES megadni ...
; ... a {:handler-key ...} tulajdonságot, amit a plugin a mutation és resolver függvények neveiben
;     névtérként használ.
;
; A plugin használatához OPCIONÁLISAN megadható ...
; ... a {:base-route "..."} tulajdonság, ami alapján a plugin regisztrálja a példa szerinti
;     "/@app-home/my-editor" útvonalat.
; ... az {:on-route ...} tulajdonság, ami a plugin által a példában regisztrált útvonalak használatakor
;     történik meg.
; ... a {:route-title ...} tulajdonság, ami a plugin által a példában regisztrált útvonalak használatakor
;     beállítódik az applikáció címkéjének.
(r/reg-event-fx
  :init-my-editor!
  [:file-editor/init-editor! :my-editor
                             {:base-route  "/@app-home/my-editor"
                              :handler-key :my-handler
                              :on-route    [:my-event]
                              :route-title "My editor"}])



;; -- A plugin használatához szükséges resolver függvények --------------------
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



;; -- A plugin használatához szükséges mutation függvények --------------------
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
