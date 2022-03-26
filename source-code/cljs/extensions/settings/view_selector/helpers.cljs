
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.settings.view-selector.helpers
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (letfn [(f [view-id] [:view-selector/change-view! :settings.view-selector view-id])]
         (let [view-id @(a/subscribe [:view-selector/get-selected-view-id :settings.view-selector])]
              [{:icon :person        :active? (= view-id :personal)      :on-click (f :personal)}
               {:icon :security      :active? (= view-id :privacy)       :on-click (f :privacy)}
               {:icon :notifications :active? (= view-id :notifications) :on-click (f :notifications)}
               {:icon :auto_awesome  :active? (= view-id :appearance)    :on-click (f :appearance)}])))
