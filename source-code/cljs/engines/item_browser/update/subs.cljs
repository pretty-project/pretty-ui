
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.update.subs
    (:require [engines.engine-handler.update.subs :as update.subs]
              [engines.item-browser.body.subs     :as body.subs]
              [engines.item-browser.core.subs     :as core.subs]
              [engines.item-browser.transfer.subs :as transfer.subs]
              [keyword.api                        :as keyword]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.update.subs
(def get-mutation-name   update.subs/get-mutation-name)
(def get-mutation-answer update.subs/get-mutation-answer)



;; -- Delete item subscriptions -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-deleted-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @example
  ; (r get-deleted-item-id :my-browser {my-handler/delete-item! "my-item"})
  ; =>
  ; "my-item"
  ;
  ; @return (string)
  [db [_ browser-id server-response]]
  (r update.subs/get-mutation-answer db browser-id :delete-item! server-response))



;; -- Duplicate item subscriptions --------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-copy-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @example
  ; (r get-duplicated-item-id :my-browser {my-handler/duplicate-item! {:my-type/id "my-item"}})
  ; =>
  ; "my-item"
  ;
  ; @return (string)
  [db [_ browser-id server-response]]
  (let [item-namespace (r transfer.subs/get-transfer-item db browser-id :item-namespace)
        copy-item      (r get-mutation-answer             db browser-id :duplicate-item! server-response)
        id-key         (keyword/add-namespace item-namespace :id)]
       (id-key copy-item)))



;; -- Undo delete item subscriptions ------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-recovered-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @example
  ; (r get-recovered-item-id :my-browser {my-handler/undo-delete-item! {:my-type/id "my-item"}})
  ; =>
  ; "my-item"
  ;
  ; @return (string)
  [db [_ browser-id server-response]]
  (let [item-namespace (r transfer.subs/get-transfer-item db browser-id :item-namespace)
        restored-item  (r get-mutation-answer             db browser-id :undo-delete-item! server-response)
        id-key         (keyword/add-namespace item-namespace :id)]
       (id-key restored-item)))



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
  ; (r parent-item-browsed? db :my-browser :delete-item! {...})
  ;
  ; @return (boolean)
  [db [_ browser-id action-key server-response]]
  ; A parent-item-browsed? függvény visszatérési értéke TRUE, ha az aktuálisan böngészett elem
  ; azonosítója megegyezik a szerver válaszából az action-key paraméterként átadott azonosítóval
  ; előállított mutation-name értékével kiolvasott dokumentból kiolvasott szűlő-elem azonosítójával.
  ;
  ; Pl.: Ha egy elemről másolat készül, akkor a szerver válasza a másolat dokumentot tartalmazza,
  ;      a másolat azonosítójával. Ahhoz, hogy megállapítható legyen, hogy a sikeres duplikálást
  ;      követően szükséges-e újratölteni az elemeket, meg kell vizsgálni, hogy a másolatot
  ;      tartalmazó elem van-e megnyitva böngészésre!
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
