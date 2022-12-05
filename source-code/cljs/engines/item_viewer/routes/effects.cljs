
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.routes.effects
    (:require [engines.item-viewer.body.subs     :as body.subs]
              [engines.item-viewer.transfer.subs :as transfer.subs]
              [re-frame.api                      :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-viewer/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  (fn [{:keys [db]} [_ viewer-id]]
      ; XXX#5068 (source-code/cljs/engines/item_handler/routes/effects.cljs)
      (let [body-did-mount? (r body.subs/body-did-mount? db viewer-id)
            on-route        (r transfer.subs/get-transfer-item db viewer-id :on-route)
            route-title     (r transfer.subs/get-transfer-item db viewer-id :route-title)]
           {:dispatch   on-route
            :dispatch-n [(if body-did-mount? [:item-viewer/load-viewer! viewer-id])
                         (if route-title     [:x.ui/set-window-title!   route-title])]})))
