
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.views
    (:require [plugins.item-editor.api :as item-editor]
              [plugins.item-lister.api :as item-lister]
              [x.app-core.api          :as a]
              [x.app-elements.api      :as elements]
              [x.app-layouts.api       :as layouts]
              [x.app-locales.api       :as locales]))



;; -- List-item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-item-secondary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [modified-at]}]
  (let [modified-at @(a/subscribe [:activities/get-actual-timestamp modified-at])]
       [:div.clients--client-item--secondary-details [:div.clients--client-item--modified-at modified-at]]))

(defn- client-item-primary-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [email-address first-name last-name] :as client-item}]
  (let [selected-language @(a/subscribe [:locales/get-selected-language])
        client-name        (locales/name->ordered-name first-name last-name selected-language)]
       [:div.clients--client-item--primary-details [:div.clients--client-item--full-name     client-name]
                                                   [:div.clients--client-item--email-address email-address]]))

(defn- client-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [client-item]
  [:div.clients--client-item--details [client-item-primary-details   client-item]
                                      [client-item-secondary-details client-item]])

(defn- client-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [client-item]
  [:div.clients--client-item [item-editor/color-stamp :clients.client-editor client-item]
                             [client-item-details client-item]])

(defn client-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ item-dex {:keys [id] :as client-item}]
  (let [on-click [:router/go-to! (str "/@app-home/clients/" id)]]
       [elements/toggle {:on-click [:item-lister/item-clicked :clients.client-lister item-dex {:on-click on-click}]
                         :content  [client-item-structure client-item]
                         :hover-color :highlight}]))



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [item-lister/header :clients.client-lister
                      {:new-item-event [:router/go-to! "/@app-home/clients/new-client"]}])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [item-lister/body :clients.client-lister
                    {:list-element #'client-item
                     :item-actions [:delete :duplicate]
                     :items-path   [:clients :client-lister/downloaded-items]
                     :search-keys  [:name :email-address]}])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  (let [description @(a/subscribe [:item-lister/get-description :clients.client-lister])]
       [layouts/layout-a surface-id {:description description
                                     :header      #'header
                                     :body        #'body}]))
