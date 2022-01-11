
(ns x.app-developer.request-browser
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.pretty    :as pretty]
              [mid-fruits.time      :as time]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-elements.api   :as elements]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-VIEW :requests)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-selected-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db (db/path :request-browser/primary :selected-view) DEFAULT-VIEW))

(defn- get-history-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [selected-view (r get-selected-view db)]
       (if-let [history-dex (get-in db (db/path :request-browser/primary :history-dex))]
               (return history-dex)
               (let [request-history (r db/get-data-history db :sync/requests selected-view)]
                    (vector/last-dex request-history)))))

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [selected-view (r get-selected-view db)]
       (if (= selected-view :requests)
           {:requests         (get-in db [:sync/requests :data-items])
            :selected-view    (param selected-view)}
           {:history-dex      (r get-history-dex           db)
            :request-history  (r db/get-data-history db :sync/requests  selected-view)
            :response-history (r db/get-data-history db :sync/responses selected-view)
            :selected-view    (param selected-view)})))

(a/reg-sub :request-browser/get-body-props get-body-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- change-view!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ view-id history-dex]]
  (-> db (assoc-in (db/path :request-browser/primary :selected-view) view-id)
         (assoc-in (db/path :request-browser/primary :history-dex)   history-dex)))

(a/reg-event-db :request-browser/change-view! change-view!)



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
                    :on-click [:request-browser/change-view! :requests]}])

(defn- go-bwd-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [history-dex request-history selected-view]}]
  (let [history-count (count request-history)]
       [elements/button {:disabled? (= history-dex 0)
                         :on-click  [:request-browser/change-view! selected-view (dec history-dex)]
                         :preset    :back-icon-button}]))

(defn- go-fwd-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [history-dex request-history selected-view]}]
  (let [history-count (count request-history)]
       [elements/button {:disabled? (= history-count (inc history-dex))
                         :on-click  [:request-browser/change-view! selected-view (inc history-dex)]
                         :preset    :forward-icon-button}]))

(defn- request-data-control-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [history-dex request-history] :as body-props}]
  [:div {:style {:display "flex" :align-items "center" :justify-content "flex-start"}}
        [go-bwd-button body-id body-props]
        [:pre {:style {:font-weight "600" :color "var( --soft-blue-xx-dark )" :font-size "14px"}}
              (get-in request-history [history-dex :sent-time])]
        [go-fwd-button body-id body-props]])

(defn- request-data-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [selected-view] :as body-props}]
  [:div {:style {:display "flex" :align-items "center" :justify-content "flex-start"}}
        [go-up-button]
        [:div {:style {:font-weight "500"}}
              (str selected-view)]])

(defn- request-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [selected-view] :as body-props}]
  [:div {:style {:width "100%"}}
        [request-data-label-bar   body-id body-props]
        [request-data-control-bar body-id body-props]
        [:div {:style {:height "24px"}}]
        [request-data             body-id body-props]
        [response-data            body-id body-props]])



;; -- Request-list components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request-id {:keys [sent-time] :as request-props} {:keys [request-failured? request-successed?]} body-props]
  [:div.x-clickable
        {:style {:width "100%" :display "flex" :justify-content "space-between" :cursor "pointer"
                 :margin "4px 0"}
         :on-click #(a/dispatch [:request-browser/change-view! request-id])}
       ;(str debug-props)
        [:div {:style {:font-weight "500" :font-size "14px" :display "flex"}}
              [:div {:style {:width "6px" :margin-right "12px" :border-radius "3px"
                             :background-color (cond request-failured?  "var( --color-warning )"
                                                     request-successed? "var( --color-success )")}}]
              (str request-id)]
        [:pre {:style {:opacity ".5" :font-size "12px"}}
              (time/timestamp-string->date-time sent-time)]])

(defn- xxx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request-id {:keys [debug] :as request-props} body-props]
  (let [debug-props (a/subscribe debug)]
       (fn [] [request-list-item request-id request-props @debug-props body-props])))

(defn- request-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [requests] :as body-props}]
  (reduce-kv #(conj %1 [xxx %2 %3 body-props])
              [:<>] requests))

(defn- request-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [selected-view] :as body-props}]
  (if (= selected-view :requests)
      [request-list body-id body-props]
      [request-view body-id body-props]))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [components/subscriber ::body
                         {:render-f   #'request-browser
                          :subscriber [:request-browser/get-body-props]}])
