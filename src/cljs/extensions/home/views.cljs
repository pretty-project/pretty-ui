
(ns extensions.home.views
    (:require [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-layouts.api    :as layouts]
              [x.app-user.api       :as user]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (maps in vector)
(def CARDS [{:label :clients      :icon :people   :on-click [:x.app-router/go-to! "/clients"]  :badge-color :secondary}
            {:label :products     :icon :category :on-click [:x.app-router/go-to! "/products"] :badge-color :secondary}
            {:label :file-storage :icon :folder   :on-click [:x.app-router/go-to! "/media"]}
            {:label :sample       :icon :none     :on-click [:x.app-router/go-to! "/sample"]}])



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:user-name (r user/get-user-name db)})

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [user-name] :as view-props}]
  (let [label (components/content {:content :welcome-n :replacements [user-name]})]
       [layouts/layout-b surface-id {:cards CARDS :label label}]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :home/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface! ::view {:content    #'view
                                  :subscriber [::get-view-props]}])

(a/reg-event-fx
  :home/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]}]
      (let [app-title (r a/get-app-detail db :app-title)]
           {:dispatch-n [[:x.app-ui/restore-default-window-title!]
                         [:x.app-ui/set-header-title! app-title]
                         [:home/render!]]})))

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:x.app-router/add-route! ::route
                                          {:route-template "/"
                                           :route-event    [:home/load!]
                                           :restricted?    true}]})
