
(ns extensions.clients.views
  (:require
   [extensions.clients.engine :as engine]
   [mid-fruits.candy         :refer [param return]]
   [mid-fruits.random        :as random]
   [mid-fruits.time          :as time]
   [plugins.item-lister.core :refer [item-lister]]
   [x.app-elements.api :as elements]
   [x.app-core.api     :as a :refer [r]]
   [x.app-locales.api :as locales]
   [x.app-ui.api :as ui]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-view-props
  [db _]
  {:selected-language (r locales/get-selected-language db)})

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-item
  [lister-id item-dex {:client/keys [added-at client-no first-name last-name email-address]}
                      {:keys [selected-language]}]
  (let [full-name (locales/name->ordered-name first-name last-name selected-language)
        added-at  (time/timestamp->date-and-time added-at :yyyymmdd :hhmm)]
       [:button.clients--client
          [:div.clients--client--primary-details
             [:div.clients--client--full-name full-name]
             [:div.clients--client--client-no client-no]]
          [:div.clients--client--secondary-details
             [:div.clients--client--email-address email-address]
             [:div.clients--client--added-at      added-at]]]))

(defn- view
  [surface-id view-props]
  [:<> [item-lister :clients {:common-props  view-props
                              :value-path    engine/CLIENTS-DATA-PATH
                              :on-list-ended [:clients/download-clients-data!]
                              :element       #'client-item
                              :on-search     [:clients/download-search-data!]
                              :request-id    :clients/download-clients-data!
                              :sortable?     true}]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-extensions.clients/render!
  [:x.app-ui/set-surface!
   ::view
   {:content #'view
    :label-bar {:content       #'ui/go-home-surface-label-bar
                :content-props {:label :clients}}
    :subscriber [::get-view-props]}])

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
