
(ns extensions.clients.client-list
    (:require [extensions.clients.engine :as engine]
              [mid-fruits.candy         :refer [param return]]
              [mid-fruits.random        :as random]
              [mid-fruits.time          :as time]
              [plugins.item-lister.core :refer [item-lister]]
              [x.app-elements.api       :as elements]
              [x.app-environment.api    :as environment]
              [x.app-core.api           :as a :refer [r]]
              [x.app-layouts.api        :as layouts]
              [x.app-locales.api        :as locales]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-SORT-BY :by-name)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-common-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:viewport-small?   (r environment/viewport-small?   db)
   :selected-language (r locales/get-selected-language db)})

(a/reg-sub ::get-common-props get-common-props)

(defn- get-header-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:search-mode?    (get-in db [:clients :list-meta :search-mode?])
   :viewport-small? (r environment/viewport-small? db)})

(a/reg-sub ::get-header-view-props get-header-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-search-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (update-in db [:clients :list-meta :search-mode?] not))

(a/reg-event-db :clients/toggle-search-mode! toggle-search-mode!)



;; -- Client-item components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-item-secondary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ {:client/keys [added-at]} _]
  (let [added-at (time/timestamp->date-and-time added-at :yyyymmdd :hhmm)]
       [:div.clients--client--secondary-details
          [:div.clients--client--added-at added-at]]))

(defn- client-item-primary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ {:client/keys [email-address first-name last-name]} {:keys [selected-language]}]
  (let [full-name (locales/name->ordered-name first-name last-name selected-language)]
       [:div.clients--client--primary-details
          [:div.clients--client--full-name     full-name]
          [:div.clients--client--email-address email-address]]))

(defn client-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id item-dex {:client/keys [id] :as client-props} common-props]
  (let [client-uri (str "/clients/" id)]
       [elements/toggle {:on-click [:x.app-router/go-to! client-uri]
                         :stretch-orientation :horizontal
                         :content
                         [:button.clients--client
                            [client-item-primary-details   lister-id item-dex client-props common-props]
                            [client-item-secondary-details lister-id item-dex client-props common-props]]}]))



;; -- Client-list header components -------------------------------------------
;; ----------------------------------------------------------------------------

(defn- quit-search-mode-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/button ::quit-search-mode-button
                   {:preset :close-icon-button :on-click [:clients/toggle-search-mode!]}])

(defn- sort-by-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/select ::sort-by-button
                   {:layout :icon-button :icon :sort :variant :transparent :color :none
                    :tooltip :sort-by :as-button? true
                    :initial-options [:by-name :by-date]
                    :options-label :order-by
                    :value-path [:clients :list-meta :sort-by]}])

(defn- add-new-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/button ::add-new-button
                   {:preset :add-icon-button :tooltip :add-new!
                    :on-click [:x.app-router/go-to! "/clients/new-client"]}])

(defn- select-more-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/button ::select-more-button
                   {:preset :select-more-icon-button :label :check}])

(defn- delete-selected-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/button ::delete-selected-button
                   {:preset :delete-icon-button :disabled? true :label :delete!}])

(defn- search-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/button ::search-button
                   {:tooltip :search :preset :search-icon-button
                    :on-click [:clients/toggle-search-mode!]}])

(defn- search-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/search-field ::search-field
                         {:placeholder :search :layout :row :auto-focus? true :min-width :xs}])

(defn- client-list-desktop-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/polarity ::desktop-header
                     {:start-content [:<> [add-new-button         surface-id view-props]
                                          [sort-by-button         surface-id view-props]]
                                         ;[select-more-button     surface-id view-props]
                                         ;[delete-selected-button surface-id view-props]
                      :end-content   [search-field surface-id view-props]}])

(defn- client-list-search-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [:div.x-search-bar-a
        [:div.x-search-bar-a--search-field
          [search-field surface-id view-props]]
        [quit-search-mode-button surface-id view-props]])

(defn- client-list-mobile-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/polarity ::mobile-header
                     {:start-content [:<> [add-new-button         surface-id view-props]
                                          [sort-by-button         surface-id view-props]]
                                         ;[select-more-button     surface-id view-props]
                                         ;[delete-selected-button surface-id view-props]
                      :end-content   [search-button surface-id view-props]}])

(defn- client-list-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [search-mode? viewport-small?] :as view-props}]
  (cond ; search-mode & small viewport
        (and viewport-small? search-mode?)
        [client-list-search-header  surface-id view-props]
        ; small viewport
        (boolean viewport-small?)
        [client-list-mobile-header  surface-id view-props]
        ; large viewport
        :desktop-header
        [client-list-desktop-header surface-id view-props]))



;; -- Client-list body components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-list-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [item-lister :clients {:on-list-ended [:clients/request-clients!]
                         :element       #'client-item
                         :request-id    :clients/synchronize-client-list!
                         ;:sortable?     true
                         :subscriber    [::get-common-props]
                         :value-path    [:clients :list-data]}])



;; -- Client-list components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [layouts/layout-a surface-id {:body   {:content    #'client-list-body}
                                :header {:content    #'client-list-header
                                         :sticky?    true
                                         :subscriber [::get-header-view-props]}}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients/render-client-list!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface! ::view {:content #'view}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:extensions/add-item-list-route! "clients" "client"]})
