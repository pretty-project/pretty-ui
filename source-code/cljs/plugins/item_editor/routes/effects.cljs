
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.routes.effects
    (:require [plugins.item-editor.mount.subs    :as mount.subs]
              [plugins.item-editor.routes.events :as routes.events]
              [plugins.item-editor.transfer.subs :as transfer.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  :item-editor/handle-route!
  (fn [{:keys [db]} [_ editor-id]]
      ; Az [:item-editor/handle-route! ...] esemény ...
      ; ... eltárolja az aktuális útvonalból származtatott item-id azonosítót.
      ; ... meghívja a szerver-oldali [:item-editor/init-editor! ...] esemény számára paraméterként
      ;     átadott {:on-route ...} eseményt.
      ;
      ; Ha a body komponens már a React-fába van csatolva, akkor meghívja az [:item-editor/request-item! ...]
      ; eseményt (az aktuális útvonalból származtatott item-id azonosító eltárolása után!), mivel a body
      ; komponens :component-did-mount életciklusa már nem fog megtörténni, ami elindítaná az elem letöltését.
      ; Pl.: Egy elem szerkesztése közben az elem duplikálása után a "Másolat szerkesztése" gombra kattintva
      ;      megtörténik az [:item-editor/edit-item! ...] esemény, ami átirányít a másolathoz tartozó
      ;      útvonalra de az útvonal kezelésekor a body komponens már a React-fába van csatolva ...
      (let [on-route (r transfer.subs/get-transfer-item db editor-id :on-route)]
           {:db          (r routes.events/handle-route! db editor-id)
            :dispatch    on-route
            :dispatch-if [(r mount.subs/body-did-mount? db editor-id)
                          [:item-editor/request-item! editor-id]]})))
