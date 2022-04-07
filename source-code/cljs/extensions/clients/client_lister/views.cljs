
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.views
    (:require [plugins.item-lister.api :as item-lister]
              [x.app-core.api          :as a]
              [x.app-layouts.api       :as layouts]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-click-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [id]}]
  (let [on-click [:router/go-to! (str "/@app-home/clients/" id)]]
       [:item-lister/item-clicked :clients.client-lister item-dex {:on-click on-click}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [lister-id item-dex {:keys [colors email-address id modified-at] :as client-item}]
  [item-lister/list-item item-dex {:icon        :navigate_next
                                   :description email-address
                                   :timestamp   modified-at
                                   :header      {:colors (or colors :placeholder)}
                                   :label      @(a/subscribe [:clients.client-lister/get-client-name item-dex])
                                   :on-click    (on-click-event item-dex client-item)}])

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
