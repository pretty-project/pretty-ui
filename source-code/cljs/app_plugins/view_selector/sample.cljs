
(ns app-plugins.view-selector.sample
    (:require [x.app-core.api :as a]
              [app-plugins.view-selector.api :as view-selector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :my-extension.view-selector/how-to-start?
  (fn [_ _]
      ; A view-selector plugin elindítható ...
      ; ... a [:view-selector/load-selector! ...] esemény meghívásával.
      [:view-selector/load-selector! :my-extension]
      ; ... a [:view-selector/go-to! ...] esemény meghívásával.
      [:view-selector/go-to! :my-extension :my-view]
      ; ... az "/@app-home/my-extension" útvonal használatával.
      [:router/go-to! "/@app-home/my-extension"]
      ; ... az "/@app-home/my-extension/my-view" útvonal használatával.
      [:router/go-to! "/@app-home/my-extension/my-view"]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :my-extension.view-selector/change-my-view!
  (fn [{:keys [db]} _]
      {:db (r view-selector/change-view! db :my-extension :my-view)
       :dispatch [:view-selector/change-view! :my-extension :my-view]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-body
  [surface-id {:keys [view-id]}]
  [:div "View-id: " view-id])

(a/reg-event-fx
  :my-extension.view-selector/render-selector!
  [:ui/set-surface! {:view {:content    #'my-body
                            :subscriber [:view-selector/get-view-props :my-extension]}}])

(a/reg-event-fx
  :my-extension.view-selector/load-selector!
  {:dispatch-n [[:ui/set-header-title! "My extension"]
                [:ui/set-window-title! "My extension"]
                [:my-extension.view-selector/render-selector!]]})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-your-view-props
  [db _]
  {:your-key "Your value"
   :view-id  (r view-selector/get-selected-view-id db :your-extension)})

(a/reg-sub :your-extension/get-your-view-props get-your-view-props)

(defn your-body
  [surface-id {:keys [view-id]}]
  [:div "View-id: " view-id])

(a/reg-event-fx
  :your-extension.view-selector/render-selector!
  [:ui/set-surface! {:view {:content    #'your-body
                            :subscriber [:your-extension/get-your-view-props]}}])

(a/reg-event-fx
  :your-extension.view-selector/load-selector!
  {:dispatch-n [[:ui/set-header-title! "My extension"]
                [:ui/set-window-title! "My extension"]
                [:your-extension.view-selector/render-selector!]]})
