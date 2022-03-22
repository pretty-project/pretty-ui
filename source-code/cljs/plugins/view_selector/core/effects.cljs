
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.effects
    (:require [plugins.view-selector.core.events   :as core.events]
              [plugins.view-selector.routes.subs   :as routes.subs]
              [plugins.view-selector.transfer.subs :as transfer.subs]
              [x.app-core.api                      :as a :refer [r]]
              [x.app-router.api                    :as router]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  (fn [{:keys [db]} [_ extension-id]]
      (let [on-route    (r transfer.subs/get-transfer-item db extension-id :on-route)
            route-title (r transfer.subs/get-transfer-item db extension-id :route-title)]
           {:db (r core.events/handle-route! db extension-id)
            :dispatch-n [on-route (if route-title [:ui/set-title! route-title])]})))



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
