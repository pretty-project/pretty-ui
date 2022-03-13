
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.effects
    (:require [x.app-core.api :as a :refer [r]]
              [app-plugins.view-selector.engine :as engine]
              [app-plugins.view-selector.events :as events]
              [app-plugins.view-selector.subs   :as subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/load-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  (fn [{:keys [db]} [_ extension-id]]
      (let [route-title (r core.subs/get-meta-item db extension-id item-namespace :route-title)
            on-load     (r core.subs/get-meta-item db extension-id item-namespace :on-load)]
           {:db (as-> db % (r core.events/load-selector! % extension-id)
                           (if-not route-title % (r ui/set-header-title! % route-title)))
            :dispatch-n [on-load (if route-title [:ui/set-window-title! route-title])]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/go-to!
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  [:view-selector/go-to! :my-extension :my-view]
  (fn [{:keys [db]} [_ extension-id view-id]]
      (if (r subs/route-handled? db extension-id)
          (let [target-route-string (engine/extended-route-string extension-id view-id)]
               [:router/go-to! target-route-string])
          [:view-selector/load-selector! extension-id {:view-id view-id}])))
