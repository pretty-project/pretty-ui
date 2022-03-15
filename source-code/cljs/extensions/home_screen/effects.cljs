
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.home-screen.effects
    (:require [extensions.home-screen.views :as views]
              [x.app-core.api               :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :home-screen/add-item!
  ; @param (keyword)(opt) item-id
  ; @param (map) item-props
  ;  {}
  ;
  ; @usage
  ;  [:home-screen/add-item!]
  (fn [_ [_ item-id item-props]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :home-screen/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! :home-screen/view {:view #'views/view}])

(a/reg-event-fx
  :home-screen/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]}]
      (let [app-title (r a/get-app-config-item db :app-title)]
           {:dispatch-n [[:ui/simulate-process!]
                         [:ui/restore-default-window-title!]
                         [:ui/set-header-title! app-title]
                         [:home-screen/render!]]})))
