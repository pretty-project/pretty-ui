
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
  :home-screen/load!
  (fn [{:keys [db]}]
      (let [app-title (r a/get-app-config-item db :app-title)]
           {:dispatch-n [[:ui/simulate-process!]
                         [:ui/restore-default-title!]
                         [:home-screen/render!]]})))

(a/reg-event-fx
  :home-screen/render!
  [:ui/render-surface! :home-screen/view
                       {:content #'views/view}])
