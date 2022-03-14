
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.effects
    (:require [plugins.view-selector.core.events :as core.events]
              [plugins.view-selector.core.subs   :as core.subs]
              [plugins.view-selector.routes.subs :as routes.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/load-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  (fn [{:keys [db]} [_ extension-id]]
      (let [on-load     (r core.subs/get-meta-item db extension-id :on-load)
            route-title (r core.subs/get-meta-item db extension-id :route-title)]
           {:db (r core.events/load-selector! db extension-id)
            :dispatch-n [on-load (if route-title [:ui/set-window-title! route-title])]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/change-view!
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  [:view-selector/change-view! :my-extension :my-view]
  (fn [{:keys [db]} [_ extension-id view-id]]
      (if-let [view-route (r routes.subs/get-view-route db extension-id view-id)]
              {:dispatch [:router/go-to! view-route]}
              {:db (r core.events/change-view! db extension-id view-id)})))