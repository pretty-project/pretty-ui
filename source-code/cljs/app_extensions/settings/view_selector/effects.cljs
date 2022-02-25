
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.settings.view-selector.effects
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-ui.api   :as ui]
              [app-extensions.settings.view-selector.views :as view-selector.views]))



;; -- Effect events -----------------------------------------------------------
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
      (if-not (r ui/element-rendered? db :surface :settings.view-selector/view)
              [:settings.view-selector/render-selector!])))
