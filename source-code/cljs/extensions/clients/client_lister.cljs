
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
              [app-plugins.item-lister.api :as item-lister]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-item-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ _ {:client/keys [added-at]}]]
  {:added-at          (r activities/get-actual-timestamp db added-at)
   :viewport-small?   (r environment/viewport-small?     db)
   :selected-language (r locales/get-selected-language   db)})

(a/reg-sub :clients/get-client-item-props get-item-props)



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

(defn- client-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex client-props item-props]
  [:div.clients--client [client-item-primary-details   item-dex client-props item-props]
                        [client-item-secondary-details item-dex client-props item-props]])

(defn client-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:client/keys [id] :as client-props}]
  (let [item-props (a/subscribe [:clients/get-client-item-props item-dex client-props])
        client-uri (item-editor/item-id->item-uri :clients :client id)]
       (fn [] [item-lister/list-item :clients :client
                                     {:on-click [:router/go-to! client-uri]
                                      :content [client-item-structure item-dex client-props @item-props]}])))



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id]
  [item-lister/header :clients :client])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [item-lister/body :clients :client {:list-element #'client-item}])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [description]}]
  [layouts/layout-a surface-id {:body   {:content #'body}
                                :header {:content #'header}
                                :description description}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients/render-client-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! ::view {:view {:content #'view :subscriber [:item-lister/get-view-props :clients]}}])
