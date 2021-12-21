
(ns app-plugins.view-selector.sample
    (:require [x.app-core.api :as a]
              [app-plugins.view-selector.api :as view-selector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/dispatch [:sync/send-request! :my-extension/synchronize! {}])

(a/dispatch [:router/go-to! "/my-extension"])
(a/dispatch [:router/go-to! "/my-extension/my-view"])

(a/dispatch [:view-selector/change-view! :my-extension :my-view])

(defn my-event [db _] (r view-selector/change-view! db :my-extension :my-view))
(a/reg-event-db :my-event my-event)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-my-props
  [db _]
  {:view-id (r view-selector/get-selected-view-id db :my-extension)})

(a/reg-sub :my-extension/get-my-props get-my-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  [surface-id {:keys [view-id]}]
  [:div (str "View-id: " view-id)])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx :my-extension/render! [:ui/set-surface! {:view {:content    #'body
                                                                :subscriber [:view-selector/get-view-props :my-extension]}}])

(a/reg-event-fx :my-extension/load! {:dispatch-n [[:ui/listen-to-process! :my-extension/synchronize!]
                                                  [:ui/set-header-title!  "My extension"]
                                                  [:ui/set-window-title!  "My extension"]
                                                  [:my-extension/render!]]})
