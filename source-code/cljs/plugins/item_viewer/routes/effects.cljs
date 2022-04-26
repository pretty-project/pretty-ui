
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.routes.effects
    (:require [plugins.item-viewer.body.subs     :as body.subs]
              [plugins.item-viewer.transfer.subs :as transfer.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  :item-viewer/handle-route!
  (fn [{:keys [db]} [_ viewer-id]]
      ; Az [:item-viewer/handle-route! ...] esemény ...
      ; ... meghívja a szerver-oldali [:item-viewer/init-viewer! ...] esemény számára
      ;     paraméterként átadott {:on-route ...} eseményt.
      ;
      ; ... ha a body komponens már a React-fába van csatolva, akkor meghívja
      ;     az [:item-viewer/load-viewer! ...] eseményt, mivel a body komponens
      ;     :component-did-mount életciklusa már nem fog újra megtörténni, ami meghívná
      ;     az [:item-viewer/load-viewer! ...] eseményt!
      ;     Pl.: Egy elem megtekintése közben az elem duplikálása után a "Másolat megtekintése"
      ;          gombra kattintva megtörténik az [:item-viewer/view-item! ...] esemény,
      ;          ami átirányít a másolathoz tartozó útvonalra de az útvonal kezelésekor
      ;          a body komponens már a React-fába van csatolva ...
      (let [body-did-mount? (r body.subs/body-did-mount?       db viewer-id)
            on-route        (r transfer.subs/get-transfer-item db viewer-id :on-route)]
           {:dispatch    on-route
            :dispatch-if [body-did-mount? [:item-viewer/load-viewer! viewer-id]]})))
