
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.subscription-browser.views
    (:require [elements.api :as elements]
              [reader.api   :as reader]
              [re-frame.api :as r]
              [vector.api   :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn subscription-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [subscription-id @(r/subscribe [:x.db/get-item [:developer-tools :subscription-browser/meta-items :current-subscription]])
        parameters      @(r/subscribe [:x.db/get-item [:developer-tools :subscription-browser/meta-items :parameters]])
        subscribed?     @(r/subscribe [:x.db/get-item [:developer-tools :subscription-browser/meta-items :subscribed?]])]
       [:div {:style {:width "100%"}}
             [:div {:style {:display :flex}}
                   [elements/icon-button ::back-button
                                         {:on-click [:x.db/remove-item! [:developer-tools :subscription-browser/meta-items]]
                                          :preset   :back}]
                   [elements/label ::subscription-id
                                   {:color       :muted
                                    :content     (str subscription-id)
                                    :font-size   :m
                                    :font-weight :extra-bold
                                    :line-height :block}]
                   [elements/icon-button {:variant :placeholder}]]
             [:div {:style {:padding "0 48px"}}
                   [elements/text-field ::subscription-vector
                                        {:indent     {:bottom :s}
                                         :label      "Parameters"
                                         :value-path [:developer-tools :subscription-browser/meta-items :parameters]}]
                   [elements/label ::subscription-vector
                                   {:color   :muted
                                    :content (str "["subscription-id (if parameters (str " " parameters)) "]")
                                    :font-size :xs
                                    :indent     {:bottom :s}
                                    :line-height :block}]
                   [elements/button ::subscribe-button
                                    {:background-color :highlight
                                     :border-radius    :xs
                                     :indent           {:bottom :m}
                                     :label            (if subscribed? "Unsubscribe" "Subscribe")
                                     :on-click         [:developer-tools.subscription-browser/toggle-subscription!]
                                     :style            {:width "240px"}}]
                   (if subscribed? [:div {:style {:padding "0 0 24px 0"}}
                                         (str @(r/subscribe (reader/string->mixed (str "["subscription-id" "parameters"]"))))])]]))

(defn subscription-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [subscription-id subscription-props]
  [:button {:style {:display :block :text-align :left :padding "0px 12px" :width "100%"} :data-clickable true
            :on-click #(r/dispatch [:x.db/set-item! [:developer-tools :subscription-browser/meta-items :current-subscription] subscription-id])}
           [:div {:style {:font-size "14px" :font-weight "500"}}
                 (str "[" subscription-id " ...]")]])

(defn subscription-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [subscription-handlers (r/get-event-handlers :sub)]
       (letfn [(f [subscription-list subscription-id]
                  (let [subscription-props (get subscription-handlers subscription-id)]
                       (conj subscription-list [subscription-list-item subscription-id subscription-props])))]
              (reduce f [:div {:style {:width "100%" :padding "0 0px"}}
                              [:div {:style {:display "flex"}}
                                    [elements/icon-button {:variant :placeholder}]
                                    [elements/label {:color       :muted
                                                     :content     "Registrated subscriptions"
                                                     :font-size   :m
                                                     :font-weight :extra-bold
                                                     :line-height :block}]]]
                        (-> subscription-handlers keys vector/abc-items)))))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [current-subscription @(r/subscribe [:x.db/get-item [:developer-tools :subscription-browser/meta-items :current-subscription]])]
          [subscription-view]
          [subscription-list]))
