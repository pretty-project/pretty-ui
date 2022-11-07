
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.routes.effects
    (:require [engines.item-handler.body.subs     :as body.subs]
              [engines.item-handler.transfer.subs :as transfer.subs]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  (fn [{:keys [db]} [_ handler-id]]
      ; XXX#5068
      ; Ha az [:item-handler/handle-route! ...] esemény megtörténésekor a body
      ; komponens már a React-fába van csatolva, akkor a body komponens
      ; component-did-mount életciklusa már nem fog újra megtörténni, ami meghívná
      ; az [:item-handler/load-handler! ...] eseményt, ezért azt az útvonal kezelésekor
      ; szükséges meghívni!
      ; Pl.: Egy elem megtekintése közben az elem duplikálása után a "Másolat megtekintése"
      ;      gombra kattintva megtörténik az [:item-handler/handle-item! ...] esemény,
      ;      ami átirányít a másolathoz tartozó útvonalra de az útvonal kezelésekor
      ;      a body komponens már a React-fába van csatolva ezért szükséges meghívni
      ;      az [:item-handler/load-handler! ...] eseményt.
      (let [body-did-mount? (r body.subs/body-did-mount?       db handler-id)
            on-route        (r transfer.subs/get-transfer-item db handler-id :on-route)
            route-title     (r transfer.subs/get-transfer-item db handler-id :route-title)]
           {:dispatch   on-route
            :dispatch-n [(if body-did-mount? [:item-handler/load-handler! handler-id])
                         (if route-title     [:ui/set-window-title! route-title])]})))
