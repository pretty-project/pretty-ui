
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.settings.view-selector.helpers
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-id @(a/subscribe [:view-selector/get-selected-view-id :settings])]
       [{:icon :person        :active? (= view-id :personal)      :on-click [:view-selector/change-view! :settings :personal]}
        {:icon :security      :active? (= view-id :privacy)       :on-click [:view-selector/change-view! :settings :privacy]}
        {:icon :notifications :active? (= view-id :notifications) :on-click [:view-selector/change-view! :settings :notifications]}
        {:icon :auto_awesome  :active? (= view-id :appearance)    :on-click [:view-selector/change-view! :settings :appearance]}]))
