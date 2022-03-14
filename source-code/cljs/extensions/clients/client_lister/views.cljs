
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
  [:div.clients--client-item [item-editor/color-stamp :clients :client client-item]
                             [client-item-details client-item]])

(defn client-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ item-dex client-item]
  [elements/toggle {:on-click [:clients.client-lister/item-clicked item-dex client-item]
                    :content  [client-item-structure client-item]
                    :hover-color :highlight}])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  (let [description @(a/subscribe [:item-lister/get-description :clients :client])]
       [layouts/layout-a :surface-id {:description description
                                      :header [item-lister/header :clients :client {:new-item-event [:router/go-to! "/@app-home/clients/new-client"]}]
                                      :body   [item-lister/body   :clients :client {:list-element #'client-item
                                                                                    :handler-key :clients.client-lister
                                                                                    :item-actions [:delete :duplicate]
                                                                                    :search-keys  [:name   :email-address]
                                                                                    :sortable? true}]}]))
