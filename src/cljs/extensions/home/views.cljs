
(ns extensions.home.views
    (:require [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-details        :as details]
              [x.app-elements.api   :as elements]
              [x.app-user.api       :as user]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:user-name                (r user/get-user-name                db)
   :user-profile-picture-url (r user/get-user-profile-picture-url db)})

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [user-name]}]
  [:div.x-header-a (components/content {:content :welcome-n :replacements [user-name]})])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [:<> [header surface-id view-props]
       [:div {:style {:display :flex :flex-wrap :wrap :justify-content :center}}
         [elements/card {:body {:content [elements/label {:content :clients :icon :people}]}
                         :horizontal-align :left
                         :on-click [:x.app-router/go-to! "/clients"]}]
         [elements/card {:body {:content [elements/label {:content :products :icon :category}]}
                         :horizontal-align :left
                         :on-click [:x.app-router/go-to! "/products"]}]
         [elements/card {:body {:content [elements/label {:content :file-storage :icon :folder}]}
                         :horizontal-align :left
                         :on-click [:x.app-router/go-to! "/media"]}]
         [elements/card {:body {:content [elements/label {:content :sample :icon :folder}]}
                         :horizontal-align :left
                         :on-click [:x.app-router/go-to! "/sample"]}]]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :home/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface! ::view {:content    #'view
                                  :subscriber [::get-view-props]}])

(a/reg-event-fx
  :home/load-home!
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
                                           :route-event    [:home/load-home!]
                                           :restricted?    true}]})
