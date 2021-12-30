
(ns app-extensions.trader.monitor
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.css       :as css]
              [mid-fruits.format    :as format]
              [mid-fruits.map       :refer [dissoc-in]]
              [mid-fruits.math      :as math]
              [mid-fruits.pretty    :as pretty]
              [mid-fruits.reader    :as reader]
              [mid-fruits.string    :as string]
              [mid-fruits.svg       :as svg]
              [mid-fruits.time      :as time]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-sync.api       :as sync]
              [x.app-elements.api   :as elements]
              [app-extensions.trader.engine :as engine]
              [app-extensions.trader.styles :as styles]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn monitor-props->diagram-points
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [kline-list price-max price-min]}]
  ; - Egy n sávos pont-diagram n+1 pont által írható le.
  ; - Az egyes sávok záró pontja az adott idősáv záró ára,
  ;   a nyító pontja az adott idősáv nyitó ára (az előző idősáv záró ára)
  ; - Az első idősáv esetén nem elérhető adat az előző idősáv záró ára,
  ;   ezért az első idősáv nyitó pontja az idősáv nyitó ára (y-open)
  (let [range (- price-max price-min)
        count (count kline-list)]
       (letfn [(f [o dex {:keys [close open]}]
                  (let [x-close        (math/percent count (inc dex))
                        y-close (- 100 (math/percent range (- close price-min)))
                        y-open  (- 100 (math/percent range (- open  price-min)))]
                       (if o (str o " "             x-close "," y-close)
                             (str o "0," y-open " " x-close "," y-close))))]
              (reduce-kv f nil kline-list))))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-monitor-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (merge (get-in db [:trader :monitor :kline-data])
         (dissoc (get-in db [:trader :monitor]) :kline-data)
         {:request-status (r sync/get-request-status    db :trader/synchronize!)
          :synchronizing? (r sync/listening-to-request? db :trader/synchronize!)}))

(a/reg-sub :trader/get-monitor-props get-monitor-props)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn highlight-kline-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ kline-item]]
  (assoc-in db [:trader :monitor :price-box] kline-item))

(a/reg-event-db :trader/highlight-kline-item! highlight-kline-item!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn server-status
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [synchronizing? watching?]}]
  [:div {:style (styles/server-status-style)}
        (if synchronizing? "connecting to server")])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn monitor-timer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [interval-duration time-left]}]
  [elements/circle-diagram {:sections [{:value time-left                       :color :primary}
                                       {:value (- interval-duration time-left) :color :highlight}]}])

(defn monitor-toggle-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [synchronizing? watching?]}]
  [elements/button ::monitor-toggle-button
                   {:disabled? synchronizing?
                    :icon      (if watching? :pause :play_arrow)
                    :on-click  [:trader/toggle-monitor!]
                    :preset    :default-icon-button}])

(defn monitor-controls
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id monitor-props]
  [:div {:style (styles/monitor-controls-style)}
        [:div {:style (styles/monitor-controls-timer-style)}
              [monitor-timer monitor-id monitor-props]]
        [monitor-toggle-button monitor-id monitor-props]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn price-box
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [price-box]}]
  (if-let [{:keys [open_timestamp close_timestamp open close]} price-box]
          [:div {:id "trader--price-box" :style (styles/price-box-style)}
                (if (= (time/timestamp-string->date close_timestamp)
                       (time/timestamp-string->date open_timestamp))
                    ; Open & close is on the same day ...
                    [:div {:style (styles/price-box-timestamps-style)}
                          [:div (time/timestamp-string->time open_timestamp)]
                          [:div (time/timestamp-string->time close_timestamp)]]
                    [:div {:style (styles/price-box-timestamps-style)}
                          [:div (time/timestamp-string->date-time open_timestamp)]
                          [:div (time/timestamp-string->date-time close_timestamp)]])
                [:div {:style (styles/price-box-prices-style)}
                      [:div (format/decimals open  2) " USD"]
                      [:div (format/decimals close 2) " USD"]]
                [:div {:style (styles/price-box-diff-style)}
                      (let [diff (- close open)]
                           (if (< 0 diff) [:span {:style (styles/price-box-pos-diff-style)}
                                                 (format/decimals (str "+" (- close open)) 2)]
                                          [:span {:style (styles/price-box-neg-diff-style)}
                                                 (format/decimals (- close open) 2)]))]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn price-sensor
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id monitor-props kline-item]
  [:div {:style (styles/price-sensor-style monitor-id monitor-props)
         :class "trader--sensor" :on-mouse-over #(a/dispatch [:trader/highlight-kline-item! kline-item])}])

(defn price-sensors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id {:keys [kline-list] :as monitor-props}]
  (letfn [(f [o x] (conj o [price-sensor monitor-id monitor-props x]))]
         (reduce f [:div {:style (styles/price-sensors-style)}]
                   (param kline-list))))

(defn- price-diagram-price-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [price-max price-min]}]
  [:div {:style (styles/price-diagram-price-data-style)}
        [:span {:style (styles/price-diagram-price-label-style)}
               (str "average:")]
        [:span {:style (styles/price-diagram-price-value-style)}
               (format/decimals (/ (+ price-max price-min) 2) 2)]
        [:span {:style (styles/price-diagram-price-label-style)}
               (str "min:")]
        [:span {:style (styles/price-diagram-price-value-style)}
               (format/decimals (+ 0 price-max) 2)]
        [:span {:style (styles/price-diagram-price-label-style)}
               (str "max:")]
        [:span {:style (styles/price-diagram-price-value-style)}
               (format/decimals (+ 0 price-min) 2)]])

(defn- price-diagram-y-labels
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [kline-list]}]
  (let [close_timestamp (:close_timestamp (last  kline-list))
        open_timestamp  (:open_timestamp  (first kline-list))]
       (if (= (time/timestamp-string->date close_timestamp)
              (time/timestamp-string->date open_timestamp))
           ; Open & close is on the same day ...
           [:div {:style (styles/price-diagram-y-labels-style)}
                 [:div (time/timestamp-string->time (:open_timestamp  (first kline-list)))]
                 [:div (time/timestamp-string->time (:close_timestamp (last  kline-list)))]]
           ; Open & close is on other days ...
           [:div {:style (styles/price-diagram-y-labels-style)}
                 [:div (time/timestamp-string->date-time (:open_timestamp  (first kline-list)))]
                 [:div (time/timestamp-string->date-time (:close_timestamp (last  kline-list)))]])))

(defn- price-diagram
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id {:keys [kline-list price-max price-min] :as monitor-props}]
  [:<> [:div {:style (styles/price-diagram-style)
              :id    "trader--price-diagram"}
             [:svg {:preserve-aspect-ratio "none" :view-box "0 0 100 100"
                    :style (styles/price-diagram-svg-style)}
                   [:polyline {:points (monitor-props->diagram-points monitor-props)
                               :style  (styles/price-diagram-line-style monitor-id monitor-props)}]]
             [price-diagram-price-data monitor-id monitor-props]
             [server-status            monitor-id monitor-props]
             [price-box                monitor-id monitor-props]
             [price-sensors            monitor-id monitor-props]
             [monitor-controls         monitor-id monitor-props]]
       [price-diagram-y-labels monitor-id monitor-props]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- monitor
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id monitor-props]
  [:<> [:style {:type "text/css"} (styles/monitor-style-rules monitor-id monitor-props)]
       [elements/horizontal-separator {:size :xxl}]
       [price-diagram monitor-id monitor-props]])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [view-id]
  [components/subscriber view-id
                         {:component  #'monitor
                          :subscriber [:trader/get-monitor-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/toggle-monitor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if-let [monitor-watching? (get-in db [:trader :monitor :watching?])]
              {:db (-> db (dissoc-in [:trader :monitor :watching?])
                          (dissoc-in [:trader :monitor :time-left]))}
              {:db (assoc-in db [:trader :monitor :watching?] true)
               :dispatch [:trader/request-kline-data!]})))

(a/reg-event-fx
  :trader/update-time!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if (get-in db [:trader :monitor :watching?])
          (let [time-left (get-in db [:trader :monitor :time-left])]
               (if (= 0 time-left) [:trader/request-kline-data!]
                                   {:db (update-in db [:trader :monitor :time-left] dec)
                                    :dispatch-later [{:ms 1000 :dispatch [:trader/update-time!]}]})))))

(a/reg-event-fx
  :trader/receive-kline-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ kline-data]]
      (let [interval          (get-in kline-data [:kline-list 0 :interval])
            interval-duration (engine/interval-duration interval)]
           {:db (-> db (assoc-in [:trader :monitor :kline-data]        kline-data)
                       (assoc-in [:trader :monitor :interval]          interval)
                       (assoc-in [:trader :monitor :interval-duration] interval-duration)
                       (assoc-in [:trader :monitor :time-left]         interval-duration))
            :dispatch-later [{:ms 1000 :dispatch [:trader/update-time!]}]
            :dispatch-n     [[:trader/resolve-kline-data!]
                             [:trader/log! :trader/monitor (get kline-data :uri)]]})))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/request-kline-data!
  (fn [{:keys [db]} _]
      (let [limit    (get-in db [:trader :settings :limit])
            symbol   (get-in db [:trader :settings :symbol   :value])
            interval (get-in db [:trader :settings :interval :value])]
           {:dispatch-n [[:sync/send-request! :trader/synchronize!
                                              {:method     :post
                                               :on-success [:trader/receive-kline-data!]
                                               :on-failure [:trader/->monitor-network-error]
                                               :uri        "/trader/query-kline-data"
                                               :params {:limit    limit
                                                        :symbol   symbol
                                                        :interval interval}}]
                         [:trader/log! :trader/monitor "Fetching data from server ..."]]})))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/->monitor-network-error
  (fn [{:keys [db]} _]
      {:db (dissoc-in db [:trader :monitor :watching?])
       :dispatch [:trader/log! :trader/monitor "Network error"]}))
