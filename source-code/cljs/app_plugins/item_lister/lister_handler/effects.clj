
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.lister-handler.effects
    (:require [app-plugins.item-lister.lister-handler.subs :as lister-handler.subs]
              [x.app-core.api                              :as a]
              [x.app-ui.api                                :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/load-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [route-title (r lister-handler.subs/get-meta-item db extension-id item-namespace :route-title)
            on-load     (r lister-handler.subs/get-meta-item db extension-id item-namespace :on-load)]
           {:db (if-not route-title db (r ui/set-header-title! db route-title))
            :dispatch-n [on-load (if route-title [:ui/set-window-title! route-title])]})))
