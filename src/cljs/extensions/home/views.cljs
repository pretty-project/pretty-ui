
(ns extensions.home.views
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-details  :as details]
              [x.app-ui.api   :as ui]
              [x.app-user.api :as user]))




;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  [db _]
  {:user-name                (r user/get-user-name                db)
   :user-profile-picture-url (r user/get-user-profile-picture-url db)})

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header
  [surface-id view-props]
  [:div (str view-props)])

(defn view
  [surface-id view-props]
  [header surface-id view-props])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :home/render!
  [:x.app-ui/set-surface! ::view {:content    #'view
                                  :label-bar  {:content       #'ui/go-home-surface-label-bar
                                               :content-props {:label details/app-name}}
                                  :subscriber [::get-view-props]}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:x.app-router/set-home! {:route-event [:home/render!]
                                          :restricted? true}]})
