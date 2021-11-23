
(ns extensions.clients.client-lister
    (:require [mid-fruits.candy      :refer [param return]]
              [x.app-activities.api  :as activities]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-layouts.api     :as layouts]
              [x.app-locales.api     :as locales]
              [app-plugins.item-editor.api :as item-editor]
              [app-plugins.item-lister.api :as item-lister :refer [item-lister]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-item-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ _ {:client/keys [added-at]}]]
  {:added-at          (r activities/get-actual-timestamp db added-at)
   :viewport-small?   (r environment/viewport-small?     db)
   :selected-language (r locales/get-selected-language   db)})

(a/reg-sub ::get-item-view-props get-item-view-props)

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (r item-lister/get-header-props db "clients"))

(a/reg-sub ::get-header-props get-header-props)

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (r item-lister/get-view-props db "clients"))

(a/reg-sub ::get-view-props get-view-props)



;; -- List-item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-item-secondary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ {:keys [added-at]}]
  [:div.clients--client--secondary-details
     [:div.clients--client--added-at added-at]])

(defn- client-item-primary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:client/keys [email-address first-name last-name]} {:keys [selected-language]}]
  (let [full-name (locales/name->ordered-name first-name last-name selected-language)]
       [:div.clients--client--primary-details
          [:div.clients--client--full-name     full-name]
          [:div.clients--client--email-address email-address]]))

(defn- xxx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:client/keys [id] :as client-props} view-props]
  (let [client-uri      (item-editor/item-id->item-uri "clients" id)]
       [elements/toggle {:on-click [:x.app-router/go-to! client-uri]
                         :stretch-orientation :horizontal
                         :content
                         [:div.clients--client
                            [client-item-primary-details   item-dex client-props view-props]
                            [client-item-secondary-details item-dex client-props view-props]]}]))

(defn client-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex client-props]
  (let [view-props (a/subscribe [::get-item-view-props item-dex client-props])]
       (fn [] [xxx item-dex client-props @view-props])))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- desktop-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id header-props]
  [elements/polarity ::desktop-header
                     {:start-content [:<> [item-lister/new-item-button    "clients" "client"]
                                          [item-lister/sort-items-button  "clients" "client"
                                                                          {:options       item-lister/DEFAULT-ORDER-BY-OPTIONS
                                                                           :initial-value item-lister/DEFAULT-ORDER-BY}]
                                          [item-lister/select-multiple-items-button         "clients"]
                                          [item-lister/toggle-item-filter-visibility-button "clients"]]
                                          ;[item-lister/delete-selected-items-button "clients" header-props]]
                      :end-content   [:<> [item-lister/search-items-field "clients" "client"]]}])

(defn- mobile-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id header-props]
  [elements/polarity ::mobile-header
                     {:start-content [:<> [item-lister/new-item-button    "clients" "client"]
                                          [item-lister/sort-items-button  "clients" "client"
                                                                          {:options       item-lister/DEFAULT-ORDER-BY-OPTIONS
                                                                           :initial-value item-lister/DEFAULT-ORDER-BY}]
                                          [item-lister/select-multiple-items-button "clients"]
                                          [item-lister/delete-selected-items-button "clients" header-props]]
                      :end-content   [:<> [item-lister/search-mode-button "clients"]]}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id {:keys [search-mode? viewport-small?] :as header-props}]
  (cond ; search-mode & small viewport
        (and viewport-small? search-mode?)
        [item-lister/search-header "clients" "client"]
        ; small viewport
        (boolean viewport-small?)
        [mobile-header  header-id header-props]
        ; large viewport
        :desktop-header
        [desktop-header header-id header-props]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [item-lister "clients" "client"
               {:list-element #'client-item}])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [description]}]
  [layouts/layout-a surface-id {:body   {:content    #'body}
                                :header {:content    #'header
                                         :sticky?    true
                                         :subscriber [::get-header-props]}
                                :description description}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients/render-client-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface! ::view {:content #'view :subscriber [::get-view-props]}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:item-lister/initialize! "clients" "client"]})
