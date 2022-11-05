
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.routes.effects
    (:require [engines.item-browser.body.subs     :as body.subs]
              [engines.item-browser.transfer.subs :as transfer.subs]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      ; XXX#5068 (engines.item-viewer.routes.effects)
      (let [body-did-mount? (r body.subs/body-did-mount? db browser-id)
            on-route        (r transfer.subs/get-transfer-item db browser-id :on-route)
            route-title     (r transfer.subs/get-transfer-item db browser-id :route-title)]
           {:dispatch   on-route
            :dispatch-n [(if body-did-mount? [:item-browser/load-browser! browser-id])
                         (if route-title     [:ui/set-window-title! route-title])]})))
