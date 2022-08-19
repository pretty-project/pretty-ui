
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



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
      ; XXX#5068
      ; Ha az [:item-viewer/handle-route! ...] esemény megtörténésekor a body komponens már a React-fába
      ; van csatolva, akkor a body komponens :component-did-mount életciklusa már nem fog újra megtörténni,
      ; ami meghívná az [:item-viewer/load-viewer! ...] eseményt, ezért az útvonal kezelésekor kell meghívni.
      ; Pl.: Egy elem megtekintése közben az elem duplikálása után a "Másolat megtekintése"
      ;      gombra kattintva megtörténik az [:item-viewer/view-item! ...] esemény,
      ;      ami átirányít a másolathoz tartozó útvonalra de az útvonal kezelésekor
      ;      a body komponens már a React-fába van csatolva ezért szükséges meghívni
      ;      az [:item-viewer/load-viewer! ...] eseményt.
      (let [body-did-mount? (r body.subs/body-did-mount? db viewer-id)
            on-route        (r transfer.subs/get-transfer-item db viewer-id :on-route)
            route-title     (r transfer.subs/get-transfer-item db viewer-id :route-title)]
           {:dispatch   on-route
            :dispatch-n [(if body-did-mount? [:item-viewer/load-viewer! viewer-id])
                         (if route-title     [:ui/set-window-title! route-title])]})))
