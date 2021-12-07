
(ns extensions.home.views
    (:require [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-layouts.api    :as layouts]
              [x.app-user.api       :as user]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (maps in vector)
(def CARDS [{:label :clients      :icon :people   :on-click [:router/go-to! "/clients"]  :badge-color :secondary}
            {:label :products     :icon :category :on-click [:router/go-to! "/products"] :badge-color :secondary}
            {:label :file-storage :icon :folder   :on-click [:router/go-to! "/media"]}
            {:label :sample       :icon :none     :on-click [:router/go-to! "/sample"]}])



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _])

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [user-name] :as view-props}]
  [layouts/layout-b surface-id {:cards CARDS}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :home/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! ::view {:view {:content #'view :subscriber [::get-view-props]}}])

(a/reg-event-fx
  :home/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]}]
      (let [app-title (r a/get-app-detail db :app-title)]
           {:dispatch-n [[:ui/restore-default-window-title!]
                         [:ui/set-header-title! app-title]
                         [:home/render!]]})))
