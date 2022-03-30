
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.update.subs
    (:require [mid-fruits.keyword                 :as keyword]
              [plugins.item-browser.core.subs     :as core.subs]
              [plugins.item-browser.mount.subs    :as mount.subs]
              [plugins.item-browser.transfer.subs :as transfer.subs]
              [plugins.plugin-handler.update.subs :as update.subs]
              [x.app-core.api                     :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.update.subs
(def get-mutation-name update.subs/get-mutation-name)



;; ----------------------------------------------------------------------------
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
  (let [mutation-name  (r get-mutation-name               db browser-id :duplicate-item!)
        item-namespace (r transfer.subs/get-transfer-item db browser-id :item-namespace)
        id-key         (keyword/add-namespace item-namespace :id)]
       (get-in server-response [(symbol mutation-name) id-key])))



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
  ; - A parent-item-browsed? függvény visszatérési értéke TRUE, ha az aktuálisan böngészett elem
  ;   azonosítója megegyezik a szerver-válaszából az action-key paraméterként átadott azonosítóval
  ;   előállított mutation-name értékével kiolvasott dokumentból kiolvasott szűlő-elem azonosítójával.
  ;
  ; - A vizsgálat elvégzéséhez szükséges, ...
  ;   ... hogy a body komponens a React-fába legyen csatolva (szükséges a :path-key tulajdonság olvasásához)!
  (boolean (if (r mount.subs/body-did-mount? db browser-id)
               (let [mutation-name   (r get-mutation-name               db browser-id action-key)
                     current-item-id (r core.subs/get-current-item-id   db browser-id)
                     item-namespace  (r transfer.subs/get-transfer-item db browser-id :item-namespace)
                     path-key        (r mount.subs/get-body-prop        db browser-id :path-key)
                     path-key        (keyword/add-namespace item-namespace path-key)
                     id-key          (keyword/add-namespace item-namespace :id)
                     document        ((symbol mutation-name) server-response)]
                    (= current-item-id (-> document path-key last id-key))))))
