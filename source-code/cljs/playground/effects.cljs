
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns playground.effects
    (:require [x.app-core.api   :as a]
              [playground.views :as views]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :playground.view-selector/render-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! :playground/view
                    {:view #'views/view}])

(a/reg-event-fx
  :playground.view-selector/load-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[:ui/set-header-title! :playground]
                [:ui/set-window-title! :playground]
                [:playground/initialize!]
                [:playground.view-selector/render-selector!]]})
                
(a/reg-event-fx
  :playground/test!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch [:developer/test!]}))
