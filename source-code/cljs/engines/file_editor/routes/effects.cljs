
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.routes.effects
    (:require [engines.file-editor.body.subs     :as body.subs]
              [engines.file-editor.transfer.subs :as transfer.subs]
              [re-frame.api                      :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :file-editor/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      ; XXX#5068 (engines.item-viewer.routes.effects)
      (let [body-did-mount? (r body.subs/body-did-mount?       db editor-id)
            on-route        (r transfer.subs/get-transfer-item db editor-id :on-route)
            route-title     (r transfer.subs/get-transfer-item db editor-id :route-title)]
           {:dispatch   on-route
            :dispatch-n [(if body-did-mount? [:file-editor/load-editor! editor-id])
                         (if route-title     [:ui/set-window-title! route-title])]})))
