
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.effects
    (:require [plugins.item-editor.core.events :as core.events]
              [plugins.item-editor.core.subs   :as core.subs]
              [plugins.item-editor.mount.subs  :as mount.subs]
              [plugins.item-editor.routes.subs :as routes.subs]
              [x.app-core.api                  :as a :refer [r]]))



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
      ; A) Ha az item-editor plugin rendelkezik az útvonal elkészítéséhez szükséges tulajdonságokkal, ...
      ;    (a szerver-oldali [:item-editor/init-editor! ...] esemény megkapta a {:base-route "..."} tulajdonságot)
      ;    ... akkor elkészíti az elemhez tartozó útvonalat és átírányít arra.
      ;    Ha a body komponens már a React-fába van csatolva, akkor az [:item-editor/handle-route! ...]
      ;    esemény feladata, hogy meghívja az [:item-editor/request-item ...] eseményt, mivel a body
      ;    komponens :component-did-mount életciklusa már nem fog megtörténni, ami elindítaná az elem
      ;    letöltését.
      ;
      ; B) Ha az item-editor plugin NEM rendelkezik az útvonal elkészítéséhez szükséges tulajdonságokkal, ...
      ;    ... akkor eltárolja a paraméterként kapott item-id azonosítót, mert az útvonal használatának hiányában
      ;        az [:item-editor/handle-route! ...] esemény nem történik meg és nem tárolja el az útvonalból
      ;        származtatott item-id azonosítót.
      ;     Ha a body komponens már a React-fába van csatolva, akkor meghívja az [:item-editor/request-item ...]
      ;     eseményt, mivel a body komponens :component-did-mount életciklusa már nem fog megtörténni ...
      (if-let [item-route (r routes.subs/get-item-route db editor-id item-id)]
              ; A)
              {:dispatch [:router/go-to! item-route]}
              ; B)
              {:db          (r core.events/store-item-id! db editor-id item-id)
               :dispatch-if [(r mount.subs/body-did-mount? db editor-id)
                             [:item-editor/request-item! editor-id]]})))
