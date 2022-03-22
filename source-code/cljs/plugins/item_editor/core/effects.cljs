
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.effects
    (:require [plugins.item-editor.core.events   :as core.events]
              [plugins.item-editor.core.subs     :as core.subs]
              [plugins.item-editor.mount.subs    :as mount.subs]
              [plugins.item-editor.routes.subs   :as routes.subs]
              [plugins.item-editor.transfer.subs :as transfer.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/edit-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-editor/edit-item! :my-extension :my-type "my-item"]
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      ; A) Ha az item-editor plugin rendelkezik az útvonal elkészítéséhez szükséges tulajdonságokkal ...
      ;    (mert a plugin szerver-oldali kezelője hozzáadta az útvonalat az útvonal-kezelőhöz)
      ;    ... akkor elkészíti az elemhez tartozó útvonalat és átírányít arra.
      ;
      ; B) Ha az item-editor plugin NEM rendelkezik az útvonal elkészítéséhez szükséges tulajdonságokkal ...
      ;    ... eltárolja a paraméterként kapott item-id azonosítót, mert az útvonal használatának hiányában
      ;        az [:item-editor/handle-route! ...] esemény nem történik meg és nem tárolja el az útvonalból
      ;        származtatott item-id azonosítót.
      ;
      ; A+B) Mindkét esetben ha a body komponens már a React-fába van csatolva, akkor meghívja
      ;      az [:item-editor/request-item ...] eseményt.
      ;      Pl.: Egy elem szerkesztése közben az elem duplikálása után a "Másolat szerkesztése" gombra kattintva
      ;           megtörténik az [:item-editor/edit-item! ...] esemény, de a body komponens már a React-fába van
      ;           csatolva, ezért a :component-did-mount esemény már nem fog megtörténni, ami elindítaná az elem
      ;           letöltését, ezért szükséges a letöltést ebben az eseményben elindítani!
      (if-let [item-route (r routes.subs/get-item-route db extension-id item-namespace item-id)]
              ; A)
              {:dispatch    [:router/go-to! item-route]
               :dispatch-if [(r mount.subs/body-did-mount? db extension-id item-namespace)
                             [:item-editor/request-item! extension-id item-namespace]]}
              ; B)
              {:db (r core.events/store-item-id! db extension-id item-namespace item-id)
               :dispatch-if [(r mount.subs/body-did-mount? db extension-id item-namespace)
                             [:item-editor/request-item! extension-id item-namespace]]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  :item-editor/handle-route!
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [on-route (r transfer.subs/get-transfer-item db extension-id item-namespace :on-route)]
           {:db (r core.events/handle-route! db extension-id item-namespace)
            :dispatch on-route})))
