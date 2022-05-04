
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
       [:div {:style {:display "flex" :padding "0" :background "white"
                      :font-weight 500
                      :height "42px" :align-items "center"
                      :border-bottom "1px solid #f0f0f0"}
              :class "ggg"}
             [:div {:style {:width "60px" :padding-left "12px" :height "24px"}}
                   [:div {:style {:height "100%" :width "6px" :border-radius "3px" :background-color (first colors)}}]]
             [:div {:style {:width "180px" :font-size "var(--font-size-s)" :font-weight 500 :color "#333"
                            :text-overflow "ellipsis" :overflow "hidden" :white-space "nowrap"}}
                   client-name]
             [:div {:style {:width "240px" :font-size "var(--font-size-xs)" :color "#555"
                            :text-overflow "ellipsis" :overflow "hidden" :white-space "nowrap"}}
                   email-address]
             [:div {:style {:width "240px" :font-size "var(--font-size-xs)" :color "#555"
                            :text-overflow "ellipsis" :overflow "hidden" :white-space "nowrap"}}
                            ;:flex-grow 1}}
                   (:phone-number client-item)]
             [:div {:style {:width "160px" :font-size "var(--font-size-xs)" :color "#555"; :text-align :right
                            :text-overflow "ellipsis" :overflow "hidden" :white-space "nowrap"}}
                   @(a/subscribe [:activities/get-actual-timestamp (:modified-at client-item)])]
             [:div {:style {:flex-grow 1 :background_ :red :display :flex
                            :justify-content :right}}

                   [elements/icon {:icon :navigate_next
                                   :size :xs}]]]))
  ;     [item-lister/list-item :clients.client-lister item-dex
  ;                            {:description email-address
  ;                             :header      {:colors (or colors :placeholder)}
  ;                             :icon        :navigate_next
  ;                             :label       client-name
  ;                             :memory-mode? true
  ;                             :on-click    [:router/go-to! (str "/@app-home/clients/" id)]
  ;                             :timestamp   modified-at}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clients-label
  []
  [:<> [surface-a/title-sensor {:title :clients :offset -12}]
       [elements/label ::clients-label
                       {:content             :clients
                        :font-size           :xxl
                        :font-weight         :extra-bold
                        :horizontal-position :left
                        :indent              {:left :xs :top_ :xxl}}]])

(defn client-list-label
  []
  (let [all-item-count @(a/subscribe [:item-lister/get-all-item-count :clients.client-lister])]
       [elements/label ::client-list-label
                       {:content   (str "Találatok ("all-item-count")")
                        :font-size :xxs
                        :color :muted
                        :indent    {:left_ :m :top :xs}}]))


(defn search-clients-field
  []
  (let [search-event [:item-lister/search-items! :clients.client-lister {:search-keys [:name :email-address]}]]
      [:div {:style {:display "flex" :justify-content "space-between"}}
        ;{:start-content
           [elements/search-field ::search-clients-field
                                 {:indent        {:top :m :bottom_ :xxs :right_ :m}; :vertical :xs}
                                  :on-empty      search-event
                                  :on-type-ended search-event
                                  :placeholder   "Keresés az ügyfelek között"
                                  ;:min-width :xl}]
                                  :style {:flex-grow 1}}]]))
         ;:end-content
         ;[client-list-label]]))

(defn add-client-button-label
  []
  [elements/label {:content   "Ügyfél létrehozása"
                   :font-size :xs
                   :icon      :add
                   :indent    {:horizontal :s :vertical :xs}}])

(defn add-client-button
  []
  [elements/card ::add-client-button
                 {:background-color :highlight
                  :border-color     :highlight
                  :border-radius    :s
                  :content #'add-client-button-label
                  :horizontal-align    :left
                  :horizontal-position :left
                  :hover-color         :highlight
                  :indent              {:left :xs :top :xxl}
                  :on-click            [:router/go-to! "/@app-home/clients/create"]
                  :style               {:width "280px"}}])

(defn order-clients-button
  []
  [elements/icon-button ::order-clients-button
                        {:border-radius :s
                         :indent        {:top :m}
                         :hover-color   :highlight
                         :on-click      [:item-lister/choose-order-by! :clients.client-lister]
                         :preset        :order-by}])

(defn client-list-header
  []
  [elements/horizontal-polarity ::client-list-header
                                {:end-content [client-list-label]}])
                            ;     :end-content   [order-clients-button]}])

(defn clients-header
  []
  [:<>
   [elements/horizontal-polarity ::client-list-header
                                {:start-content [clients-label]}]])
                                 ;:end-content   [client-list-label]}]])

(defn create-client-button
  []
  [:div {:style {:position :fixed :bottom 0 :right 0}}
        [elements/icon-button ::xxx
                              {:preset :add
                               :background-color :highlight
                               :border-radius :xxl
                               :border-color :muted
                               :hover-color :highlight
                               :indent {:all :m}
                               :color :primary}]])




;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-list
  []
  [:<> [item-lister/body :clients.client-lister
                         {:default-order-by :modified-at/descending
                          :items-path   [:clients :client-lister/downloaded-items]
                          :list-element #'client-item}]])
       ;[elements/horizontal-separator {:size :xxl}]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn xxx
  [{:keys [label order-by-key]}]
  (if-let [current-order-by @(a/subscribe [:item-lister/get-current-order-by :clients.client-lister])]
          (if-let [current-key? (= (namespace current-order-by) (name order-by-key))]
                  (let [direction (name current-order-by)]
                       [elements/button {:color            :default
                                         :label            label
                                         :font-size        :xs
                                         :horizontal-align :left
                                         :icon (if (= direction "descending") :arrow_drop_down :arrow_drop_up)
                                         :icon-position    :right
                                         :on-click (if (= direction "descending")
                                                       [:item-lister/order-items! :clients.client-lister (keyword (name order-by-key) "ascending")]
                                                       [:item-lister/order-items! :clients.client-lister (keyword (name order-by-key) "descending")])}])
                  [elements/button {:color            :highlight
                                    :label            label
                                    :font-size        :xs
                                    :horizontal-align :left
                                    :on-click [:item-lister/order-items! :clients.client-lister (keyword (name order-by-key) "descending")]}])))

(defn x
  []
  (if-let [current-order-by @(a/subscribe [:item-lister/get-current-order-by :clients.client-lister])]
       [:div {:style {:display "flex" :position "sticky" :padding "48px 0 0 0" :background "white"
                      :font-weight 500 :font-size "var(--font-size-xs)" :top "18px"
                      ;:color "var(--color-muted)"
                      :height "90px"
                      :border-bottom "1px solid #ddd"
                      :margin-bottom "12px"}}
             [:div {:style {:width "60px"}}]
             [:div {:style {:width "180px"}}
                   [xxx {:label :name :order-by-key :name}]]
             [:div {:style {:width "240px"}}
                   [xxx {:label :email-address :order-by-key :email-address}]]
             [:div {:style {:width "240px"}}
                   [xxx {:label :phone-number :order-by-key :phone-number}]]
             [:div {:style {:width "160px"}}
                [xxx {:label :last-modified :order-by-key :modified-at}]]]))

(defn t
  []
  [:div {:style {}};:position "absolute"}}])
        "t"])


(defn view-structure
  []
  [:<> ;[clients-label]
       ;[add-client-button]
       [elements/horizontal-separator {:size :xxl}]
       [clients-header]

       ;[elements/horizontal-separator {:size :xxl}]
       [search-clients-field]
       [client-list-header]



       [:style {:type "text/css"} ".ggg:hover { background: #f5f5f5 !important; cursor: pointer } "]

       ;[:div {:style {:position "sticky" :top "60px" :z-index "9999"}}
             ;[client-list]
             ;[t]
        ;     [:div {:style {:height "calc(100vh - 72px)" :background "#f0f0f0" :width "300px"
        ;                    :overflow "auto" :border "1px solid red"
        ;           [:div {:style {:background-image "linear-gradient(180deg, #fff,#000)"
                                  ;:height "1900px"]
                   ;[:div {:style {:overflow "auto" :height "100%" :width "10px" :background :black}}]]]

       ;[:div {:style {:background-image "linear-gradient(#fff,#000)"
        ;              :height "1900px"]])
;
       [:div {:style {:display :flex :flex-direction :column-reverse}}
             [:div {:style {:width "100%"}}
                   [client-list]]
             [x]]
      [create-client-button]])

(defn view
  [surface-id]
  [surface-a/layout surface-id {:content [view-structure]}])
