
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.views
    (:require [plugins.item-lister.api :as item-lister]
              [x.app-core.api          :as a]
              [x.app-elements.api      :as elements]
              [x.app-layouts.api       :as layouts]
              [x.app-ui.api            :as ui]))



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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [item-lister/header :clients.client-lister
                      {:item-actions [:delete :duplicate]
                       :new-item-event [:router/go-to! "/@app-home/clients/new-client"]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [ui/title-sensor {:title :clients}]
       [elements/label {:content     :clients
                        :font-size   :xl
                        :font-weight :extra-bold
                        :indent      {:top :xxl}}]])

(defn- body-description
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [description @(a/subscribe [:item-lister/get-description :clients.client-lister])]
       [elements/label {:color     :muted
                        :content   description
                        :font-size :xxs}]))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [body-label]
       [body-description]
       [item-lister/body :clients.client-lister
                         {:items-path   [:clients :client-lister/downloaded-items]
                          :list-element #'client-item
                          :search-keys  [:name :email-address]}]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [layouts/layout-a surface-id
                    {:body   #'body
                     :header #'header}])
