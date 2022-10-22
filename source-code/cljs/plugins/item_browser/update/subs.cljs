
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.update.subs
    (:require [mid-fruits.keyword                 :as keyword]
              [plugins.item-browser.body.subs     :as body.subs]
              [plugins.item-browser.core.subs     :as core.subs]
              [plugins.item-browser.transfer.subs :as transfer.subs]
              [plugins.plugin-handler.update.subs :as update.subs]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.update.subs
(def get-mutation-name   update.subs/get-mutation-name)
(def get-mutation-answer update.subs/get-mutation-answer)



;; -- Duplicate item subscriptions --------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-copy-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @example
  ;  (r update.subs/get-copy-id :my-browser {my-handler/duplicate-item! {:my-type/id "my-item"}})
  ;  =>
  ;  "my-item"
  ;
  ; @return (string)
  [db [_ browser-id server-response]]
  (let [item-namespace  (r transfer.subs/get-transfer-item db browser-id :item-namespace)
        duplicated-item (r get-mutation-answer             db browser-id :duplicate-item! server-response)
        id-key          (keyword/add-namespace item-namespace :id)]
       (id-key duplicated-item)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn parent-item-browsed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (keyword) action-key
  ; @param (map) server-response
  ;
  ; @usage
  ;  (r update.subs/parent-item-browsed? db :my-browser :delete-item! {...})
  ;
  ; @return (boolean)
  [db [_ browser-id action-key server-response]]
  ; A parent-item-browsed? függvény visszatérési értéke TRUE, ha az aktuálisan böngészett elem
  ; azonosítója megegyezik a szerver-válaszából az action-key paraméterként átadott azonosítóval
  ; előállított mutation-name értékével kiolvasott dokumentból kiolvasott szűlő-elem azonosítójával.
  ;
  ; A vizsgálat elvégzéséhez szükséges, ...
  ; ... hogy a body komponens a React-fába legyen csatolva (szükséges a :path-key tulajdonság olvasásához)!
  (boolean (if (r body.subs/body-did-mount? db browser-id)
               (let [answered-item   (r get-mutation-answer db browser-id action-key server-response)
                     current-item-id (r core.subs/get-current-item-id   db browser-id)
                     item-namespace  (r transfer.subs/get-transfer-item db browser-id :item-namespace)
                     path-key        (r body.subs/get-body-prop         db browser-id :path-key)
                     path-key        (keyword/add-namespace item-namespace path-key)
                     id-key          (keyword/add-namespace item-namespace :id)]
                    (= current-item-id (-> answered-item path-key last id-key))))))
