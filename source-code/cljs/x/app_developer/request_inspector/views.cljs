
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.request-inspector.views
    (:require [mid-fruits.pretty  :as pretty]
              [mid-fruits.time    :as time]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]))



;; -- Request-view components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [request-props @(a/subscribe [:developer/get-request-props])]
       [:div {:style {:margin-bottom "48px"}}
             [:div {:style {:font-weight "600" :line-height "32px"}}
                   (str "Request details:")]
             [:pre {:style {:font-size "12px" :line-height "18px"}}
                   (pretty/mixed->string request-props)]]))

(defn- response-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [request-response @(a/subscribe [:developer/get-request-props])]
       [:div {}
             [:div {:style {:font-weight "600" :line-height "32px"}}
                   (str "Server response:")]
             [:pre {:style {:font-size "12px" :line-height "18px"}}
                   (pretty/mixed->string request-response)]]))

(defn- go-up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button {:preset   :back-icon-button
                    :on-click [:request-inspector/show-requests!]}])

(defn- go-bwd-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [history-dex request-history selected-view]}]
  (let [request-history-dex @(a/subscribe [:developer/get-request-history-dex])]
       [elements/button {:disabled? (= request-history-dex 0)
                         :on-click  [:request-inspector/inspect-prev-request!]
                         :preset    :back-icon-button}]))

(defn- go-fwd-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [request-history-count @(a/subscribe [:developer/get-request-history-count])
        request-history-dex   @(a/subscribe [:developer/get-request-history-dex])]
       [elements/button {:disabled? (= request-history-count (inc request-history-dex))
                         :on-click  [:request-inspector/inspect-next-request!]
                         :preset    :forward-icon-button}]))

(defn- request-data-control-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [request-sent-time @(a/subscribe [:developer/get-request-prop :sent-time])]
       [:div {:style {:display "flex" :align-items "center" :justify-content "flex-start"}}
             [go-bwd-button]
             [:pre {:style {:font-weight "600" :color "var( --soft-blue-xx-dark )" :font-size "14px"}}
                   (str request-sent-time)]
             [go-fwd-button]]))

(defn- request-data-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [request-id @(a/subscribe [:developer/get-inspected-request-id])]
       [:div {:style {:display "flex" :align-items "center" :justify-content "flex-start"}}
             [go-up-button]
             [:div {:style {:font-weight "500"}}
                   (str request-id)]]))

(defn- request-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div {:style {:width "100%"}}
        [request-data-label-bar]
        [request-data-control-bar]
        [:div {:style {:height "24px"}}]
        [request-data]
        [response-data]])



;; -- Request-list components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request-id]
  (let [request-failured?  @(a/subscribe [:sync/request-failured?     request-id])
        request-successed? @(a/subscribe [:sync/request-successed?    request-id])
        request-sent-time  @(a/subscribe [:sync/get-request-sent-time request-id])]
       [:div.x-clickable
             {:style {:width "100%" :display "flex" :justify-content "space-between" :cursor "pointer"
                      :margin "4px 0"}
              :on-click #(a/dispatch [:request-inspector/inspect-request! request-id])}
             [:div {:style {:font-weight "500" :font-size "14px" :display "flex"}}
                   [:div {:style {:width "6px" :margin-right "12px" :border-radius "3px"
                                  :background-color (cond request-failured?  "var( --color-warning )"
                                                          request-successed? "var( --color-success )")}}]
                   (str request-id)]
             [:pre {:style {:opacity ".5" :font-size "12px"}}
                   (time/timestamp-string->date-time request-sent-time)]]))

(defn- request-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [request-ids @(a/subscribe [:developer/get-request-ids])]
       (reduce #(conj %1 [request-list-item %2])
                [:<>] request-ids)))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [request-id @(a/subscribe [:developer/get-inspected-request-id])]
          [request-view]
          [request-list]))
