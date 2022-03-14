
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.sample
    (:require [plugins.view-selector.api :as view-selector]
              [x.app-core.api            :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :my-extension.view-selector/change-my-view!
  (fn [{:keys [db]} _]
      {:db (r view-selector/change-view! db :my-extension :my-view)
       :dispatch [:view-selector/change-view! :my-extension :my-view]}))



;; -- A plugin használata -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-content
  [extension-id]
  (let [view-id @(a/subscribe [:view-selector/get-selected-view-id extension-id])]
       (case view-id :my-view   [:div "My view"]
                     :your-view [:div "Your view"]
                                [:div "Default view"])))

(defn my-view
  [surface-id]
  [view-selector/view :my-extension {:allowed-view-ids [:my-view :your-view]
                                     :content #'my-content
                                     :default-view-id :my-view}])
