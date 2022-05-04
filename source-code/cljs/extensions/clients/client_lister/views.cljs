
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.views
    (:require [layouts.surface-a.api   :as surface-a]
              [plugins.item-lister.api :as item-lister]
              [x.app-core.api          :as a]
              [x.app-elements.api      :as elements]
              [x.app-layouts.api       :as layouts]
              [x.app-ui.api            :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-item
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
  []
  [:<> [surface-a/title-sensor {:title :clients :offset 36}]
       [elements/label ::clients-label
                       {:content             :clients
                        :font-size           :xxl
                        :font-weight         :extra-bold
                        :horizontal-position :left
                        :indent              {:left :xs :top :xxl}}]])

(defn- search-clients-field
  []
  (let [search-event [:item-lister/search-items! :clients.client-lister {:search-keys [:name :email-address]}]]
       [elements/search-field ::search-clients-field
                             {:indent        {:top :xxl :vertical :xs}
                              :on-empty      search-event
                              :on-type-ended search-event
                              :placeholder   "Keresés az ügyfelek között"}]))

(defn- add-client-button
  []
  [elements/card ::add-client-button
                 {:background-color :highlight
                  :border-color     :highlight
                  :border-radius    :s
                  :content [elements/label {:content   "Ügyfél létrehozása"
                                            :font-size :xs
                                            :icon      :add
                                            :indent    {:horizontal :s :vertical :xs}}]
                  :horizontal-align    :left
                  :horizontal-position :left
                  :hover-color         :highlight
                  :indent              {:left :xs :top :xxl}
                  :on-click            [:router/go-to! "/@app-home/clients/create"]
                  :style               {:width "280px"}}])

(defn- client-list-label
  []
  (let [all-item-count @(a/subscribe [:item-lister/get-all-item-count :clients.client-lister])]
       [elements/label ::client-list-label
                       {:content   (str "Találatok ("all-item-count")")
                        :font-size :xs
                        :indent    {:left :m :top :xxl}}]))

(defn- order-clients-button
  []
  [elements/select ::order-clients-button
                   {:indent {:top :m}
                    :layout :icon-button
                    :hover-color :highlight
                    :border-radius :s
                    :initial-options ["x"]
                    :initial-value "y"
                    :label "xxx"
                    :required? true
                    :helper "Lorem ipsum dolor set ..."
                    :info-text "xxx"
                    :options-label "xxxx"}])


                    ;:preset :order-by}])

(defn- client-list-header
  []
  [elements/horizontal-polarity ::client-list-header
                                {;:start-content [client-list-label]
                                 :start-content   [order-clients-button]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-list
  []
  [:<> [item-lister/body :clients.client-lister
                         {:items-path   [:clients :client-lister/downloaded-items]
                          :list-element #'client-item}]
       [elements/horizontal-separator {:size :xxl}]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  []
  [:<> [clients-label]
       [search-clients-field]
       [add-client-button]
       [client-list-header]
       [client-list]])

(defn view
  [surface-id]
  [surface-a/layout surface-id {:content [view-structure]}])
