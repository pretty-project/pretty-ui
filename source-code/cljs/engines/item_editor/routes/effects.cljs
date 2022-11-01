
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.routes.effects
    (:require [engines.item-editor.body.subs     :as body.subs]
              [engines.item-editor.transfer.subs :as transfer.subs]
              [re-frame.api                      :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-editor/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      ; XXX#5068
      (let [body-did-mount? (r body.subs/body-did-mount?       db editor-id)
            on-route        (r transfer.subs/get-transfer-item db editor-id :on-route)
            route-title     (r transfer.subs/get-transfer-item db editor-id :route-title)]
           {:dispatch   on-route
            :dispatch-n [(if body-did-mount? [:item-editor/load-editor! editor-id])
                         (if route-title     [:ui/set-window-title! route-title])]})))
