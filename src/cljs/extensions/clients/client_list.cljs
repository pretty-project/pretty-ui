
(ns extensions.clients.client-list
    (:require [extensions.clients.engine :as engine]
              [extensions.pattern        :as pattern]
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
  (r pattern/get-item-list-header-view-props db "clients"))

(a/reg-sub ::get-header-view-props get-header-view-props)



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

(defn- client-list-desktop-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/polarity ::desktop-header
                     {:start-content [:<> [pattern/item-list-add-new-item-button "clients" "client"]
                                          [pattern/item-list-sort-items-button   "clients" {:options [:by-name :by-date]}]]
                                         ;[pattern/item-list-select-multiple-items-button "clients"]
                                         ;[pattern/item-list-delete-selected-items-button "clients"]
                      :end-content   [:<> [pattern/item-list-search-items-field  "clients"]]}])

(defn- client-list-mobile-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/polarity ::mobile-header
                     {:start-content [:<> [pattern/item-list-add-new-item-button "clients" "client"]
                                          [pattern/item-list-sort-items-button   "clients" {:options [:by-name :by-date]}]]
                                         ;[pattern/item-list-select-multiple-items-button "clients"]
                                         ;[pattern/item-list-delete-selected-items-button "clients"]
                      :end-content   [:<> [pattern/item-list-search-mode-button  "clients"]]}])

(defn- client-list-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [search-mode? viewport-small?] :as view-props}]
  (cond ; search-mode & small viewport
        (and viewport-small? search-mode?)
        [pattern/item-list-search-header "clients"]
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
