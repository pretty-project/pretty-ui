
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.config-editor.routes.effects
    (:require [plugins.config-editor.body.subs     :as body.subs]
              [plugins.config-editor.transfer.subs :as transfer.subs]
              [x.app-core.api                      :as a :refer [r]]))
 


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  :config-editor/handle-route!
  (fn [{:keys [db]} [_ editor-id]]
      ; XXX#5068
      (let [body-did-mount? (r body.subs/body-did-mount?       db editor-id)
            on-route        (r transfer.subs/get-transfer-item db editor-id :on-route)
            route-title     (r transfer.subs/get-transfer-item db editor-id :route-title)]
           {:dispatch   on-route
            :dispatch-n [(if body-did-mount? [:config-editor/load-editor! editor-id])
                         (if route-title     [:ui/set-window-title! route-title])]})))
