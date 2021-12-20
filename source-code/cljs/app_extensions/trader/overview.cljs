
(ns app-extensions.trader.overview
    (:require [mid-fruits.css       :as css]
              [mid-fruits.math      :as math]
              [mid-fruits.string    :as string]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.api   :as elements]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-overview-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:api-key         (get-in db [:trader :api-key])
  ;:synchronized-at (get-in db [:trader :synchronized-at])})
   :synchronized-at 1581231260})

(a/reg-sub :trader/get-overview-props get-overview-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- api-key
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [api-key]}]
  [:<> [elements/label {:content "API key" :layout :fit :font-size :xxs :color :muted}]
       [elements/button ::api-key-button
                        {:label  (if (string/nonempty? api-key) "********************" "No key added")
                         :preset (if (string/nonempty? api-key) :primary-button :warning-button)
                         :on-click [:value-editor/load! {:label "API key" :value-path [:trader :api-key]
                                                         :required? false}]}]])

(defn- symbol-c
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ overview-props]
  [:<> [elements/label {:content "SYMBOL" :layout :fit :font-size :xxs :color :muted}]
       [elements/button ::api-key-button
                        {:label "ETHUSD" :preset :primary-button}]])

(defn- settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id overview-props]
  [:div {:style {:border "2px solid var( --border-color-highlight )" :padding "12px" :border-radius "var( --border-radius-m )"
                 :width "180px"}}
        [elements/horizontal-separator {:size :m}]
        [api-key  body-id overview-props]
        [elements/horizontal-separator {:size :m}]
        [symbol-c body-id overview-props]])

(defn- ignition-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id overview-props]
  [:div {:style {:width "128px" :height "128px" :background-color "var( --background-color-highlight )"
                 :border-radius "64px" :display :flex :flex-direction :column :justify-content :center
                 :align-items :center :line-height "128px" :font-size "var( --font-size-xs )"
                 :font-weight 600 :box-shadow "0 0 10px rgba(0, 0, 0, .2)" :color "var( --color-muted )"
                 :border "4px solid var( --border-color-primary )"
                 :cursor :pointer}}
        "START TRADER"])

(defn- price-history-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [max-price min-price synchronized-at] :as overview-props} {:keys [price timestamp]}]
  (let [price-range   (- max-price min-price)
        price-range   (- 4120.21   4082.32)
        price-offset  (- price     min-price)
        price-offset  (- price     4082.32)
        price-percent (math/percent price-range price-offset)]
       [:<> [:div {:style {:font-family "monospace" :color "#555" :display :flex :justify-content :space-between}}
                  [:span {:style {:color "var( --color-muted )" :font-size "12px" :line-height "20px"}}
                         (/ (- synchronized-at timestamp) 60) "m"]
                  [:span {:style {:font-size "14px" :line-height "20px"}}
                         price]]
            [:div
              [:div {:style {:position :absolute :width "100%" :height "100%" :top 0 :left 0 :background-color "#e5a9e9"
                             :border-radius "2px"}}]
              [:div {:style {:width (css/percent price-percent) :background-color "#72e997" :height "4px"
                             :margin "0 0 18px 0"
                             :border-radius "2px"}}]]]))

(defn overview
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id overview-props]
  [:<> [:div {:style {:display :flex :justify-content :space-between :width "100%" :margin "24px 0" :align-items :center}}
             [settings        body-id overview-props]
             [:div [ignition-button   body-id overview-props]]
                   ;[monitoring-button body-id overview-props]]
             [:div {:style {:border "2px solid var( --border-color-highlight )" :padding "12px" :border-radius "var( --border-radius-m )"
                            :width "180px"}}]]
       [:div {:style {:display :flex :justify-content :space-between :width "100%" :margin "24px 0" :align-items :center}}

             [:div {:style {:width "180px" :background-color "var( --background-color-highlight )"
                            :border-radius "var( --border-radius-m )" :padding "12px"
                            :border "1px solid var( --border-color-muted )"}}
                   [:div {:style {:font-size "var( --font-size-xs )" :margin "0 0 12px 0" :text-align :center
                                  :color "var( --color-muted )"}}
                         "Price history"]
                   [price-history-item body-id overview-props {:price 4120.21 :timestamp "1581231200"}]
                   [price-history-item body-id overview-props {:price 4112.01 :timestamp "1581231140"}]
                   [price-history-item body-id overview-props {:price 4100.41 :timestamp "1581231080"}]
                   [price-history-item body-id overview-props {:price 4082.32 :timestamp "1581231020"}]
                   [price-history-item body-id overview-props {:price 4103.45 :timestamp "1581230960"}]]
             [:div]
             [:div]]])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [components/subscriber body-id
                         {:component  #'overview
                          :subscriber [:trader/get-overview-props]}])
