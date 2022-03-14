
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.developer-tools.helpers
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-id @(a/subscribe [:gestures/get-selected-view-id :developer.developer-tools/handler])]
       [{:label    "DB"
         :on-click [:gestures/change-view! :developer.developer-tools/handler :re-frame-browser]
         :active?  (= view-id :re-frame-browser)}
        {:label    "Requests"
         :on-click [:gestures/change-view! :developer.developer-tools/handler :request-inspector]
         :active?  (= view-id :request-inspector)}
        {:label    "Routes"
         :on-click [:gestures/change-view! :developer.developer-tools/handler :route-browser]
         :active?  (= view-id :route-browser)}]))