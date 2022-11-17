
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.request-inspector.views
    (:require [elements.api :as elements]
              [pretty.print :as pretty]
              [re-frame.api :as r]
              [reagent.api  :refer [ratom]]
              [time.api     :as time]
              [vector.api   :as vector]))



;; -- Request-view components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (letfn [(f [%] (cond-> % (:error-handler-f    %) (assoc :error-handler-f    "...")
                           (:response-f         %) (assoc :response-f         "...")
                           (:progress-handler-f %) (assoc :progress-handler-f "...")
                           (:response-handler-f %) (assoc :response-handler-f "...")
                           (:validator-f        %) (assoc :validator-f        "...")))]
         (let [show-more?    (ratom false)
               request-props (r/subscribe [:developer-tools.request-inspector/get-request-props])]
              (fn []
                  [:div {:style {:margin-bottom "48px"}}
                        [:div {:style {:font-weight "600" :line-height "32px"}}
                              (str "Request details:")]
                        [:pre {:style {:font-size "12px" :line-height "18px"}}
                              (pretty/mixed->string (if @show-more? @request-props (f @request-props)))]
                        (if (not= @request-props (f @request-props))
                            [:div {:style {:cursor "pointer" :font-size "12px" :padding-top "12px" :font-weight "500" :width "120px" :color "var(--color-primary)"}
                                   :data-clickable "true" :on-click #(swap! show-more? not)
                                   :data-selectable "false"}
                                  (if @show-more? "Show less" "Show more")])]))))

(defn- response-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [request-response @(r/subscribe [:developer-tools.request-inspector/get-request-response])]
       [:div {}
             [:div {:style {:font-weight "600" :line-height "32px"}}
                   (str "Server response:")]
             [:pre {:style {:font-size "12px" :line-height "18px"}}
                   (pretty/mixed->string request-response)]]))

(defn- go-up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/icon-button {:on-click [:developer-tools.request-inspector/show-requests!]
                         :preset   :back}])

(defn- go-bwd-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [history-dex request-history selected-view]}]
  (let [request-history-dex @(r/subscribe [:developer-tools.request-inspector/get-request-history-dex])]
       [elements/icon-button {:disabled? (= request-history-dex 0)
                              :on-click  [:developer-tools.request-inspector/inspect-prev-request!]
                              :preset    :back}]))

(defn- go-fwd-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [request-history-count @(r/subscribe [:developer-tools.request-inspector/get-request-history-count])
        request-history-dex   @(r/subscribe [:developer-tools.request-inspector/get-request-history-dex])]
       [elements/icon-button {:disabled? (= request-history-count (inc request-history-dex))
                              :on-click  [:developer-tools.request-inspector/inspect-next-request!]
                              :preset    :forward}]))

(defn- request-data-control-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [request-sent-time @(r/subscribe [:developer-tools.request-inspector/get-request-prop :sent-time])]
       [:div {:style {:display "flex" :align-items "center" :justify-content "flex-start"}}
             [go-bwd-button]
             [:pre {:style {:font-weight "600" :color "var( --soft-blue-xx-dark )" :font-size "14px"}}
                   (str request-sent-time)]
             [go-fwd-button]]))

(defn- request-data-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [request-id @(r/subscribe [:developer-tools.request-inspector/get-inspected-request-id])]
       [:div {:style {:display "flex" :align-items "center" :justify-content "flex-start"}}
             [go-up-button]
             [:div {:style {:font-weight "500"}}
                   (str request-id)]]))

(defn- request-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div {:style {:padding "12px" :width "100%"}}
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
  (let [request-failured?  @(r/subscribe [:x.sync/request-failured?     request-id])
        request-successed? @(r/subscribe [:x.sync/request-successed?    request-id])
        request-sent-time  @(r/subscribe [:x.sync/get-request-sent-time request-id])]
       [:div {:data-clickable true
              :style {:width "100%" :display "flex" :justify-content "space-between" :cursor "pointer"
                      :margin "4px 0" :grid-column-gap "24px"}
              :on-click #(r/dispatch [:developer-tools.request-inspector/inspect-request! request-id])}
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
  (let [request-ids @(r/subscribe [:developer-tools.request-inspector/get-request-ids])]
       (reduce #(conj %1 [request-list-item %2])
                [:div {:style {:padding "12px"}}]
                request-ids)))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [request-id @(r/subscribe [:developer-tools.request-inspector/get-inspected-request-id])]
          [request-view]
          [request-list]))
