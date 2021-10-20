
(ns extensions.clients.views
    (:require [x.app-core.api :as a]
              [plugins.item-lister.core :refer [item-lister]]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  []
  [:div#z-u812.x-column {:data-vertical-align :center}
   [:div {:style {:color "#333" :paddingBottom "60px" :fontSize "50px" :lineHeight "100px" :fontWeight "500"}}
    "monotech"]
   [:div {:style {:fontWeight "500"}}
    "SZAKKEZESSZEGED.HU"
    [:span {:style {:color "#555"}}
     "#540675"]]
   [:div.x-row {:data-horizontal-align :center :style {:padding "20px 0 60px"}}
    [:div.x-icon {:style {:marginRight "12px"}} "lock"]
    [:div {:style {:lineHeight "24px"}}
     "This client has no public information."]]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-extensions.clients/render!
  [:x.app-ui/set-surface!
   ::view
   {:content #'item-lister
    :content-props {:value-path [:a-items]
                    :download-items-event [:clients/download-clients!]}}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot
   {:dispatch-n [[:x.app-router/add-route!
                  ::route
                  {:restricted?    true
                   :route-event    [:x.app-extensions.clients/render!]
                   :route-template "/clients"}]
                 [:x.app-router/add-route!
                  ::extended-route
                  {:restricted?    true
                   :route-event    [:x.app-extensions.clients/render!]
                   :route-template "/clients/:client-id"}]]}})
