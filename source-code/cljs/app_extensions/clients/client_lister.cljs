
(ns app-extensions.clients.client-lister
    (:require [mid-fruits.candy      :refer [param return]]
              [x.app-activities.api  :as activities]
              [x.app-core.api        :as a :refer [r]]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-locales.api     :as locales]
              [app-plugins.item-editor.api :as item-editor]
              [app-plugins.item-lister.api :as item-lister]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-item-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ _ {:keys [modified-at]}]]
  {:modified-at       (r activities/get-actual-timestamp db modified-at)
   :viewport-small?   (r environment/viewport-small?     db)
   :selected-language (r locales/get-selected-language   db)})

(a/reg-sub :clients/get-client-item-props get-item-props)



;; -- List-item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-item-secondary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ {:keys [modified-at]}]
  [:div.clients--client-item--secondary-details [:div.clients--client-item--modified-at modified-at]])

(defn- client-item-primary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [email-address first-name last-name]} {:keys [selected-language]}]
  (let [full-name (locales/name->ordered-name first-name last-name selected-language)]
       [:div.clients--client-item--primary-details [:div.clients--client-item--full-name     full-name]
                                                   [:div.clients--client-item--email-address email-address]]))

(defn- client-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex client-props item-props]
  [:div.clients--client-item--details [client-item-primary-details   item-dex client-props item-props]
                                      [client-item-secondary-details item-dex client-props item-props]])

(defn- client-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex client-props item-props]
  [:div.clients--client-item [item-editor/color-stamp :clients :client client-props]
                             [client-item-details item-dex client-props item-props]])

(defn client-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [id] :as client-props}]
  (let [item-props (a/subscribe [:clients/get-client-item-props item-dex client-props])
        client-uri (item-editor/editor-uri :clients :client id)]
       (fn [] [elements/toggle {:on-click [:router/go-to! client-uri]
                                :content  [client-item-structure item-dex client-props @item-props]
                                :hover-color :highlight}])))



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [description]}]
  [item-lister/view :clients :client  {:list-element #'client-item}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients/render-client-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! ::view {:view {:content #'view}}])
