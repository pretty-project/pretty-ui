
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.download.effects
    (:require [plugins.plugin-handler.core.subs :as core.subs]
              [x.app-core.api                   :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :plugin-handler/set-auto-title!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  (fn [{:keys [db]} [_ plugin-id]]
      ; ...
      (let [item-label (r core.subs/get-current-item-label db plugin-id)]
           [:ui/set-window-title! item-label])))
