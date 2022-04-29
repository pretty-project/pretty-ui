
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
                               :memory-mode? true
                               :on-click    [:router/go-to! (str "/@app-home/clients/" id)]
                               :timestamp   modified-at}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- clients-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [data-received? @(a/subscribe [:item-lister/data-received? :clients.client-lister])]
          [:<> [ui/title-sensor {:title :clients}]
               [elements/label ::clients-label
                               {:content     :clients
                                :font-size   :xl
                                :font-weight :extra-bold
                                :indent      {:top :xxl}}]]))

(defn- clients-items-info-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [data-received? @(a/subscribe [:item-lister/data-received? :clients.client-lister])]
          (let [items-info @(a/subscribe [:item-lister/get-items-info :clients.client-lister])]
               [elements/label ::clients-items-info-label
                               {:color     :muted
                                :content   items-info
                                :font-size :xxs}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [item-lister/header :clients.client-lister
                      {:new-item-event [:router/go-to! "/@app-home/clients/create"]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [clients-label]
       [clients-items-info-label]
       [elements/horizontal-separator {:size :xxl}]
       [item-lister/body :clients.client-lister
                         {:item-actions [:delete :duplicate]
                          :items-path   [:clients :client-lister/downloaded-items]
                          :list-element #'client-item
                          :search-keys  [:name :email-address]}]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [layouts/layout-a ::view
                    {:body   #'body
                     :header #'header}])
