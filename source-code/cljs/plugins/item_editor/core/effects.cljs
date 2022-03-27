
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
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-editor/edit-item! :my-editor "my-item"]
  (fn [{:keys [db]} [_ editor-id item-id]]
      ; A) Ha az item-editor plugin rendelkezik az útvonal elkészítéséhez szükséges tulajdonságokkal ...
      ;    (a szerver-oldali [:item-editor/init-editor! ...] esemény megkapta a {:route-template "..."} tulajdonságot)
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
      (if-let [item-route (r routes.subs/get-item-route db editor-id item-id)]
              ; A)
              {:dispatch    [:router/go-to! item-route]
               :dispatch-if [(r mount.subs/body-did-mount? db editor-id)
                             [:item-editor/request-item! editor-id]]}
              ; B)
              {:db (r core.events/store-item-id! db editor-id item-id)
               :dispatch-if [(r mount.subs/body-did-mount? db editor-id)
                             [:item-editor/request-item! editor-id]]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  :item-editor/handle-route!
  (fn [{:keys [db]} [_ editor-id]]
      (let [on-route (r transfer.subs/get-transfer-item db editor-id :on-route)]
           {:db (r core.events/handle-route! db editor-id)
            :dispatch on-route})))
