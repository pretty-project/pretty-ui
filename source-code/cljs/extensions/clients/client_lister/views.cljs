
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.views
    (:require [layouts.surface-a.api   :as surface-a]
              [mid-fruits.keyword      :as keyword]
              [plugins.item-lister.api :as item-lister]
              [x.app-core.api          :as a]
              [x.app-elements.api      :as elements]
              [x.app-ui.api            :as ui]

              ; TEMP
              [x.app-layouts.api :as layouts]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clients-label
  []
  [:<> [surface-a/title-sensor {:title :clients :offset 36}]
       [elements/label ::clients-label
                       {:content     :clients
                        :font-size   :xxl
                        :font-weight :extra-bold
                        :indent      {:top :xxl}}]])

(defn search-clients-field
  []
  (let [search-event [:item-lister/search-items! :clients.client-lister {:search-keys [:email-address :name :phone-number]}]]
       [elements/search-field ::search-clients-field
                              {:indent        {:top :s}
                               :on-empty      search-event
                               :on-type-ended search-event
                               :placeholder   "Keresés az ügyfelek között"}]))

(defn client-list-description
  []
  (let [all-item-count @(a/subscribe [:item-lister/get-all-item-count :clients.client-lister])]
       [elements/label ::client-list-label
                       {:color            :muted
                        :content          (str "Találatok ("all-item-count")")
                        :font-size        :xxs
                        :horizontal-align :right
                        :indent           {:top :xs}}]))

(defn create-client-button
  []
  [:div {:style {:position :fixed :bottom 0 :right 0}}
        [elements/icon-button ::create-client-button
                              {:border-color  :muted
                               :border-radius :xxl
                               :color         :primary
                               :hover-color   :highlight
                               :indent        {:all :m}
                               :on-click      [:router/go-to! "/@app-home/clients/create"]
                               :preset        :add}]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-item-structure
  [lister-id item-dex {:keys [colors email-address id modified-at phone-number]}]
  (let [client-name @(a/subscribe [:clients.client-lister/get-client-name item-dex])]
       [:div {:style {:display "flex" :font-weight 500
                      :height "48px" :align-items "center"
                      :border-bottom "1px solid #f0f0f0"}}
             [:div {:style {:width "48px"}}
                   [elements/color-marker {:colors colors
                                           :indent {:left :xs}
                                           :size   :m}]]
             [:div {:style {:flex-grow 1}}
                   [elements/label {:color   "#333"
                                    :content client-name
                                    :indent  {:horizontal :xs :right :xs}}]]
             [:div {:style {:width "240px"}}
                   [elements/label {:content   email-address
                                    :color     "#555"
                                    :font-size :xs
                                    :indent    {:horizontal :xs :right :xs}}]]
             [:div {:style {:width "240px"}}
                   [elements/label {:content   phone-number
                                    :color     "#555"
                                    :font-size :xs
                                    :indent    {:horizontal :xs :right :xs}}]]
             [:div {:style {:width "160px"}}
                   [elements/label {:content @(a/subscribe [:activities/get-actual-timestamp modified-at])
                                    :color     "#555"
                                    :font-size :xs
                                    :indent    {:horizontal :xs :right :xs}}]]
             [:div {:style {}}
                   [elements/icon {:icon   :navigate_next
                                   :indent {:right :xs}
                                   :size   :xs}]]]))

(defn client-item
  [lister-id item-dex {:keys [id] :as client-item}]
  [elements/toggle {:content     [client-item-structure lister-id item-dex client-item]
                    :hover-color :highlight
                    :on-click    [:router/go-to! (str "/@app-home/clients/"id)]}])

(defn client-list
  []
  [item-lister/body :clients.client-lister
                    {:default-order-by :modified-at/descending
                     :items-path       [:clients :client-lister/downloaded-items]
                     :list-element     #'client-item}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-list-column-label
  [{:keys [label order-by-key]}]
  (let [current-order-by @(a/subscribe [:item-lister/get-current-order-by :clients.client-lister])
        current-order-by-key       (keyword/get-namespace current-order-by)
        current-order-by-direction (keyword/get-name      current-order-by)]
       [elements/button {:color (if (= order-by-key current-order-by-key) :default :muted)
                         :icon  (if (= order-by-key current-order-by-key)
                                    (case current-order-by-direction :descending :arrow_drop_down :ascending :arrow_drop_up))
                         :on-click (if (= order-by-key current-order-by-key)
                                       [:item-lister/swap-items!  :clients.client-lister]
                                       [:item-lister/order-items! :clients.client-lister (keyword/add-namespace order-by-key :descending)])
                         :label            label
                         :font-size        :xs
                         :horizontal-align :left
                         :icon-position    :right
                         :indent           {:horizontal :xxs}}]))

(defn client-list-header
  []
  (if-let [data-received? @(a/subscribe [:item-lister/data-received? :clients.client-lister])]
          [:div {:style {:background-color "white"  :border-bottom "1px solid #ddd" :display "flex"
                         :position         "sticky" :top           "48px"}}
                [:div {:style {:width "48px"}}]
                [:div {:style {:display "flex" :flex-grow 1}}   [client-list-column-label {:label :name          :order-by-key :name}]]
                [:div {:style {:display "flex" :width "240px"}} [client-list-column-label {:label :email-address :order-by-key :email-address}]]
                [:div {:style {:display "flex" :width "240px"}} [client-list-column-label {:label :phone-number  :order-by-key :phone-number}]]
                [:div {:style {:display "flex" :width "160px"}} [client-list-column-label {:label :last-modified :order-by-key :modified-at}]]
                [:div {:style {:width "36px"}}]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view-structure
  []
  [:<> [clients-label]
       [search-clients-field]
       [client-list-description]
       [elements/horizontal-separator {:size :xxl}]
       [:div {:style {:display :flex :flex-direction :column-reverse}}
             [:div {:style {:width "100%"}}
                   [client-list]]
             [client-list-header]]
      [elements/horizontal-separator {:size :xxl}]
      [create-client-button]])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'view-structure}])
