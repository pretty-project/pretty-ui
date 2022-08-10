
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns settings.view-selector.helpers
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (letfn [(f [view-id] [:view-selector/change-view! :settings.view-selector view-id])]
         (let [current-view-id @(a/subscribe [:view-selector/get-current-view-id :settings.view-selector])]
              [{:icon :person        :active? (= current-view-id :personal)      :on-click (f :personal)}
               {:icon :security      :active? (= current-view-id :privacy)       :on-click (f :privacy)}
               {:icon :notifications :active? (= current-view-id :notifications) :on-click (f :notifications)}
               {:icon :auto_awesome  :active? (= current-view-id :appearance)    :on-click (f :appearance)}])))
