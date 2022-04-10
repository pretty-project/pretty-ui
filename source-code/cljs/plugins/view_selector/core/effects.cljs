
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.effects
    (:require [plugins.view-selector.core.events :as core.events]
              [plugins.view-selector.routes.subs :as routes.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/change-view!
  ; @param (keyword) selector-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  [:view-selector/change-view! :my-selector :my-view]
  (fn [{:keys [db]} [_ selector-id view-id]]
      (if-let [view-route (r routes.subs/get-view-route db selector-id view-id)]
              {:dispatch [:router/go-to! view-route]}
              {:db (r core.events/change-view! db selector-id view-id)})))
