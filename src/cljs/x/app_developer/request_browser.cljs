
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.07
; Description:
; Version: v0.2.0
; Compatibility: x4.4.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.request-browser
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.pretty    :as pretty]
              [mid-fruits.time      :as time]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-elements.api   :as elements]
              [x.app-sync.api       :as sync]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-VIEW :requests)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-selected-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db (db/path ::primary :selected-view) DEFAULT-VIEW))

(defn- get-history-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [selected-view (r get-selected-view db)]
       (if-let [history-dex (get-in db (db/path ::primary :history-dex))]
               (return history-dex)
               (let [request-history (r sync/get-request-history db selected-view)]
                    (vector/last-dex request-history)))))

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [selected-view (r get-selected-view db)]
       (if (= selected-view :requests)
           {:requests         (r sync/get-requests         db)
            :selected-view    (param selected-view)}
           {:history-dex      (r get-history-dex           db)
            :request-history  (r sync/get-request-history  db selected-view)
            :response-history (r sync/get-response-history db selected-view)
            :selected-view    (param selected-view)})))

(a/reg-sub ::get-view-props get-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- change-view!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ view-id history-dex]]
  (-> db (assoc-in (db/path ::primary :selected-view) view-id)
         (assoc-in (db/path ::primary :history-dex)   history-dex)))

(a/reg-event-db ::change-view! change-view!)



;; -- Request-view components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [history-dex selected-view request-history]}]
  (let [request (vector/nth-item request-history history-dex)
        request (dissoc request :debug)]
       [:div {:style {:margin-bottom "48px"}}
             [:div {:style {:font-weight "600" :line-height "32px"}}
                   (str "Request details:")]
             [:pre {:style {:font-size "12px" :line-height "18px"}}
                   (pretty/mixed->string request)]]))

(defn- response-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [history-dex selected-view response-history]}]
  (let [response (vector/nth-item response-history history-dex)]
       [:div {}
             [:div {:style {:font-weight "600" :line-height "32px"}}
                   (str "Server response:")]
             [:pre {:style {:font-size "12px" :line-height "18px"}}
                   (pretty/mixed->string response)]]))

(defn- go-up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button {:preset   :back-icon-button
                    :on-click [::change-view! :requests]}])

(defn- go-bwd-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [history-dex request-history selected-view]}]
  (let [history-count (count request-history)]
       [elements/button {:disabled? (= history-dex 0)
                         :on-click  [::change-view! selected-view (dec history-dex)]
                         :preset    :back-icon-button}]))

(defn- go-fwd-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [history-dex request-history selected-view]}]
  (let [history-count (count request-history)]
       [elements/button {:disabled? (= history-count (inc history-dex))
                         :on-click  [::change-view! selected-view (inc history-dex)]
                         :preset    :forward-icon-button}]))

(defn- request-data-control-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [component-id {:keys [history-dex request-history] :as view-props}]
  [:div {:style {:display "flex" :align-items "center" :justify-content "flex-start"}}
        [go-bwd-button component-id view-props]
        [:pre {:style {:font-weight "600" :color "var( --soft-blue-xx-dark )" :font-size "14px"}}
              (get-in request-history [history-dex :sent-time])]
        [go-fwd-button component-id view-props]])

(defn- request-data-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [component-id {:keys [selected-view] :as view-props}]
  [:div {:style {:display "flex" :align-items "center" :justify-content "flex-start"}}
        [go-up-button]
        [:div {:style {:font-weight "500"}}
              (str selected-view)]])

(defn- request-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [component-id {:keys [selected-view] :as view-props}]
  [:div {:style {:width "100%"}}
        [request-data-label-bar   component-id view-props]
        [request-data-control-bar component-id view-props]
        [:div {:style {:height "24px"}}]
        [request-data             component-id view-props]
        [response-data            component-id view-props]])



;; -- Request-list components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request-id {:keys [sent-time] :as request-props} {:keys [request-failured? request-successed?] :as debug-props} view-props]
  [:div.x-toggle
        {:style {:width "100%" :display "flex" :justify-content "space-between" :cursor "pointer"
                 :margin "4px 0"}
         :on-click #(a/dispatch [::change-view! request-id])}
       ;(str debug-props)
        [:div {:style {:font-weight "500" :font-size "14px" :display "flex"}}
              [:div {:style {:width "6px" :margin-right "12px" :border-radius "3px"
                             :background-color (cond request-failured?  "var( --color-warning )"
                                                     request-successed? "var( --color-success )")}}]
              (str request-id)]
        [:pre {:style {:opacity ".5" :font-size "12px"}}
              (time/timestamp-string->date-and-time sent-time)]])

(defn- xxx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request-id {:keys [debug] :as request-props} view-props]
  (let [debug-props (a/subscribe debug)]
       (fn [] [request-list-item request-id request-props @debug-props view-props])))

(defn- request-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [component-id {:keys [requests] :as view-props}]
  (reduce-kv (fn [%1 %2 %3]
                 (vector/conj-item %1 [xxx %2 %3 view-props]))
             [:<>]
             (param requests)))

(defn- request-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [component-id {:keys [selected-view] :as view-props}]
  (if (= selected-view :requests)
      [request-list component-id view-props]
      [request-view component-id view-props]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [components/subscriber ::view
                         {:component  #'request-browser
                          :subscriber [::get-view-props]}])
