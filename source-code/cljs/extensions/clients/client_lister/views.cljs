
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.views
    (:require [plugins.item-lister.api :as item-lister]
              [x.app-core.api          :as a]
              [x.app-layouts.api       :as layouts]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id item-dex {:keys [colors email-address id modified-at] :as client-item}]
  (let [client-name @(a/subscribe [:clients.client-lister/get-client-name item-dex])]
       [item-lister/list-item :clients.client-lister item-dex
                              {:description email-address
                               :header      {:colors (or colors :placeholder)}
                               :icon        :navigate_next
                               :label       client-name
                               :on-click    [:router/go-to! (str "/@app-home/clients/" id)]
                               :timestamp   modified-at}]))

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [item-lister/header :clients.client-lister
                      {:new-item-event [:router/go-to! "/@app-home/clients/new-client"]}])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [item-lister/body :clients.client-lister
                    {:item-actions [:delete :duplicate]
                     :items-path   [:clients :client-lister/downloaded-items]
                     :list-element #'client-item
                     :search-keys  [:name :email-address]}])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  (let [description @(a/subscribe [:item-lister/get-description :clients.client-lister])]
       [layouts/layout-a surface-id
                         {:body        #'body
                          :description description
                          :header      #'header}]))
