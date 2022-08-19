
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.routes.effects
    (:require [plugins.item-browser.body.subs     :as body.subs]
              [plugins.item-browser.transfer.subs :as transfer.subs]
              [x.app-core.api                     :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      ; XXX#5068
      (let [body-did-mount? (r body.subs/body-did-mount? db browser-id)
            on-route        (r transfer.subs/get-transfer-item db browser-id :on-route)
            route-title     (r transfer.subs/get-transfer-item db browser-id :route-title)]
           {:dispatch   on-route
            :dispatch-n [(if body-did-mount? [:item-browser/load-browser! browser-id])
                         (if route-title     [:ui/set-window-title! route-title])]})))
