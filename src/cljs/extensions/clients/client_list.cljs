
(ns extensions.clients.client-list
    (:require [extensions.clients.engine :as engine]
              [mid-fruits.candy         :refer [param return]]
              [mid-fruits.random        :as random]
              [mid-fruits.time          :as time]
              [plugins.item-lister.core :refer [item-lister]]
              [x.app-elements.api       :as elements]
              [x.app-core.api           :as a :refer [r]]
              [x.app-layouts.api        :as layouts]
              [x.app-locales.api        :as locales]
              [x.app-ui.api             :as ui]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-SORT-BY :by-name)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-view-props
  [db _]
  {:selected-language (r locales/get-selected-language db)})

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sort-by-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id lister-props]
  [elements/button {:layout :icon-button :icon :sort :variant :transparent :color :none
                    :label :sort-by :on-click [:item-lister/render-sort-by-options! lister-id]}])

(defn add-new-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id lister-props]
  [elements/button {:layout :icon-button :icon :add :variant :transparent
                    :label :add-new! :on-click [:x.app-router/go-to! "új elem és új kategória"]}])

(defn select-more-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id lister-props]
  [elements/button {:layout :icon-button :variant :transparent :icon :radio_button_unchecked
                    :color :none :disabled? false :label :check}])

(defn delete-selected-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id lister-props]
  [elements/button {:layout :icon-button :variant :transparent :icon :delete_outline
                    :color :warning :disabled? true :label :delete!}])

(defn search-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id {:keys [on-search] :as lister-props}]
  [:div#clients--client-list--search-field
    [elements/search-field {:placeholder :search :layout :fit :stretch-orientation :horizontal
                            :on-type-ended on-search}]])

(defn search-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id {:keys [on-search] :as lister-props}]
  [:div#clients--client-list--search-bar
    [:div.x-icon-buttons
      [add-new-button         lister-id lister-props]
      [sort-by-button         lister-id lister-props]
      [select-more-button     lister-id lister-props]]
      ;[delete-selected-button lister-id lister-props]]
    [search-field lister-id lister-props]])

(defn client-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id item-dex {:client/keys [added-at client-no id first-name last-name email-address]}
                      {:keys [selected-language]}]
  (let [full-name  (locales/name->ordered-name first-name last-name selected-language)
        added-at   (time/timestamp->date-and-time added-at :yyyymmdd :hhmm)
        client-uri (str "/clients/" id)]
       [:button.clients--client {:on-click #(a/dispatch [:x.app-router/go-to! client-uri])}
          [:div.clients--client--primary-details
             [:div.clients--client--full-name full-name]
             [:div.clients--client--client-no "#" client-no]]
          [:div.clients--client--secondary-details
             [:div.clients--client--email-address email-address]
             [:div.clients--client--added-at      added-at]]]))

(defn- client-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ view-props]
  [item-lister :clients {:common-props  view-props
                         :on-list-ended [:clients/request-clients!]
                         :element       #'client-item
                         :request-id    :clients/request-clients!
                         ;:sortable?     true
                         :value-path    engine/CLIENTS-DATA-PATH}])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [layouts/layout-a surface-id {:label :clients :icon :people
                                :body        {:content       #'client-list
                                              :content-props view-props}
                                :body-header {:content       #'search-bar
                                              :content-props view-props}}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/render-sort-by-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-elements/render-select-options!
    ::order-by-select-options
    {:default-value DEFAULT-SORT-BY
     :options-label :order-by
     :options [{:label :by-name  :value :by-name}
               {:label :by-date  :value :by-date}
               {:label :by-size  :value :by-size}
               {:label :by-order :value :by-order}]}])

(a/reg-event-fx
  :x.app-extensions.clients/render-clients-list!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface!
   ::view {:content   #'view
           :label-bar {:content       #'ui/go-home-surface-label-bar
                       :content-props {:label :clients}}
           :subscriber [::get-view-props]}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:x.app-router/add-route!
                 ::route
                 {:restricted?    true
                  :route-event    [:x.app-extensions.clients/render-clients-list!]
                  :route-title    :clients
                  :route-template "/clients"}]})
