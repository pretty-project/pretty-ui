
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.settings.view-selector.effects
    (:require [extensions.settings.view-selector.views :as view-selector.views]
              [x.app-core.api                          :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings.view-selector/render-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! :settings.view-selector/view
                    {:view #'view-selector.views/view}])

(a/reg-event-fx
  :settings.view-selector/load-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      [:settings.view-selector/render-selector!]))
