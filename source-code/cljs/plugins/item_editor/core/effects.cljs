
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.effects
    (:require [plugins.item-editor.core.events   :as core.events]
              [plugins.item-editor.transfer.subs :as transfer.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  :item-editor/load-editor!
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      ; XXX#4579
      (let [on-load     (r transfer.subs/get-transfer-item db extension-id item-namespace :on-load)
            route-title (r transfer.subs/get-transfer-item db extension-id item-namespace :route-title)]
           {:db (r core.events/load-editor! db extension-id item-namespace)
            :dispatch-n [on-load (if route-title [:ui/set-window-title! route-title])]})))
