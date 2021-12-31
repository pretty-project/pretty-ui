
(ns app-extensions.trader.monitor
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.css       :as css]
              [mid-fruits.format    :as format]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.loop      :refer [reduce-indexed]]
              [mid-fruits.map       :as map :refer [dissoc-in]]
              [mid-fruits.math      :as math]
              [mid-fruits.pretty    :as pretty]
              [mid-fruits.reader    :as reader]
              [mid-fruits.random    :as random]
              [mid-fruits.string    :as string]
              [mid-fruits.svg       :as svg]
              [mid-fruits.time      :as time]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-sync.api       :as sync]
              [x.app-elements.api   :as elements]
              [app-fruits.react-transition  :as react-transition]
              [app-extensions.trader.engine :as engine]
              [app-extensions.trader.styles :as styles]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def DEFAULT-SYMBOL {:label "ETH / USD" :value "ETHUSD"})

; @constant (map)
(def DEFAULT-INTERVAL {:label "3 minutes" :value "3"})

; @constant (integer)
(def DEFAULT-LIMIT 180)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn generate-monitor-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (keyword/add-namespace :monitor (random/generate-keyword)))

(defn- monitor-props->diagram-points
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

(defn- monitor-watching?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ monitor-id]]
  (get-in db [:trader :monitor monitor-id :watching?]))

(defn- get-time-left
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ monitor-id]]
  (get-in db [:trader :monitor monitor-id :time-left]))

(defn- time-elapsed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ monitor-id]]
  (= 0 (r get-time-left db monitor-id)))

(defn- get-monitor-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ monitor-id]]
  {:limit    (get-in db [:trader :monitor monitor-id :settings :limit])
   :symbol   (get-in db [:trader :monitor monitor-id :settings :symbol   :value])
   :interval (get-in db [:trader :monitor monitor-id :settings :interval :value])})

(defn- get-monitor-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ monitor-id]]
  (merge (get-in db [:trader :monitor monitor-id])
         {:synchronizing? (r sync/listening-to-request? db :trader/synchronize!)}))

(a/reg-sub :trader/get-monitor-props get-monitor-props)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-monitor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (update-in db [:trader :monitor :monitor-ids] vector/conj-item (generate-monitor-id)))

(a/reg-event-db :trader/add-monitor! add-monitor!)

(defn- remove-monitor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ monitor-id]]
  (update-in db [:trader :monitor :monitor-ids] vector/remove-item monitor-id))

(a/reg-event-db :trader/remove-monitor! remove-monitor!)

(defn- init-monitor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ monitor-id]]
  (-> db (assoc-in [:trader :monitor monitor-id :settings-mode?] true)
         (assoc-in [:trader :monitor monitor-id :settings]
                   {:symbol   DEFAULT-SYMBOL
                    :limit    DEFAULT-LIMIT
                    :interval DEFAULT-INTERVAL})))

(a/reg-event-db :trader/init-monitor! init-monitor!)

(defn- store-received-kline-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ monitor-id kline-data]]
  (update-in db [:trader :monitor monitor-id] merge kline-data))

(defn- init-timer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ monitor-id]]
  (let [interval (get-in db [:trader :monitor monitor-id :kline-list 0 :interval])
        interval-duration (engine/interval-duration interval)]
       (-> db (assoc-in [:trader :monitor monitor-id :interval]          interval)
              (assoc-in [:trader :monitor monitor-id :interval-duration] interval-duration)
              (assoc-in [:trader :monitor monitor-id :time-left]         interval-duration))))

(defn- pause-monitor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ monitor-id]]
  (-> db (dissoc-in [:trader :monitor monitor-id :watching?])
         (dissoc-in [:trader :monitor monitor-id :time-left])))

(defn- start-monitor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ monitor-id]]
  (assoc-in db [:trader :monitor monitor-id :watching?] true))

(defn- show-monitor-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ monitor-id]]
  (assoc-in db [:trader :monitor monitor-id :settings-mode?] true))

(a/reg-event-db :trader/show-monitor-settings! show-monitor-settings!)

(defn- show-monitor-chart!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ monitor-id]]
  (assoc-in db [:trader :monitor monitor-id :settings-mode?] false))

(a/reg-event-db :trader/show-monitor-chart! show-monitor-chart!)

(defn- update-time!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ monitor-id]]
  (update-in db [:trader :monitor monitor-id :time-left] dec))

(defn- highlight-kline-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ monitor-id kline-item]]
  (assoc-in db [:trader :monitor monitor-id :price-box] kline-item))

(a/reg-event-db :trader/highlight-kline-item! highlight-kline-item!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- monitor-timer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [interval-duration time-left]}]
  [elements/circle-diagram {:sections [{:value time-left                       :color :primary}
                                       {:value (- interval-duration time-left) :color :highlight}]}])

(defn- monitor-toggle-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id {:keys [synchronizing? watching?]}]
  [elements/button {:disabled? synchronizing?
                    :icon      (if watching? :pause :play_arrow)
                    :on-click  [:trader/toggle-monitor! monitor-id]
                    :preset    :default-icon-button}])

(defn- monitor-tl-controls
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id monitor-props]
  [:div {:style (styles/monitor-tl-controls-style)}
        [:div {:style (styles/monitor-controls-timer-style)}
              [monitor-timer monitor-id monitor-props]]
        [monitor-toggle-button monitor-id monitor-props]])

(defn- monitor-bl-controls
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id {:keys [settings-mode?] :as monitor-props}]
  [:div {:style (styles/monitor-bl-controls-style)}
        (if settings-mode? [elements/button {:icon :show_chart
                                             :preset :default-icon-button
                                             :on-click [:trader/show-monitor-chart! monitor-id]}]
                           [elements/button {:icon :settings
                                             :preset :default-icon-button
                                             :on-click [:trader/show-monitor-settings! monitor-id]}])])

(defn- add-monitor-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/button {:icon :add
                    :preset :primary-icon-button
                    :on-click [:trader/add-monitor!]}])

(defn- remove-monitor-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id _]
  [elements/button {:icon :close
                    :preset :warning-icon-button
                    :on-click [:trader/remove-monitor! monitor-id]}])

(defn- monitor-br-controls
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id {:keys [first-monitor? last-monitor?] :as monitor-props}]
  [:div {:style (styles/monitor-br-controls-style)}
        (if     last-monitor?  [add-monitor-button    monitor-id monitor-props])
        (if-not first-monitor? [remove-monitor-button monitor-id monitor-props])])

(defn- monitor-controls
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id monitor-props]
  [:<> [monitor-tl-controls monitor-id monitor-props]
       [monitor-bl-controls monitor-id monitor-props]
       [monitor-br-controls monitor-id monitor-props]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-box
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [price-box]}]
  (if-let [{:keys [open-timestamp close-timestamp open close]} price-box]
          [:div {:class "trader--price-box" :style (styles/price-box-style)}
                (if (= (time/timestamp-string->date close-timestamp)
                       (time/timestamp-string->date open-timestamp))
                    ; Open & close is on the same day ...
                    [:div {:style (styles/price-box-timestamps-style)}
                          [:div (time/timestamp-string->time open-timestamp)]
                          [:div (time/timestamp-string->time close-timestamp)]]
                    [:div {:style (styles/price-box-timestamps-style)}
                          [:div (time/timestamp-string->date-time open-timestamp)]
                          [:div (time/timestamp-string->date-time close-timestamp)]])
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

(defn- price-sensor
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id monitor-props kline-item]
  [:div {:style (styles/price-sensor-style monitor-id monitor-props)
         :class "trader--sensor" :on-mouse-over #(a/dispatch [:trader/highlight-kline-item! monitor-id kline-item])}])

(defn- price-sensors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id {:keys [kline-list] :as monitor-props}]
  (letfn [(f [o x] (conj o [price-sensor monitor-id monitor-props x]))]
         (reduce f [:div {:style (styles/price-sensors-style)}]
                   (param kline-list))))

(defn- monitor-chart-price-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [price-max price-min]}]
  [:div {:style (styles/monitor-chart-price-data-style)}
        [:span {:style (styles/monitor-chart-price-label-style)}
               (str "average:")]
        [:span {:style (styles/monitor-chart-price-value-style)}
               (format/decimals (/ (+ price-max price-min) 2) 2)]
        [:span {:style (styles/monitor-chart-price-label-style)}
               (str "min:")]
        [:span {:style (styles/monitor-chart-price-value-style)}
               (format/decimals (+ 0 price-max) 2)]
        [:span {:style (styles/monitor-chart-price-label-style)}
               (str "max:")]
        [:span {:style (styles/monitor-chart-price-value-style)}
               (format/decimals (+ 0 price-min) 2)]])

(defn- monitor-chart-y-labels
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [kline-list]}]
  (let [close-timestamp (:close-timestamp (last  kline-list))
        open-timestamp  (:open-timestamp  (first kline-list))]
       (if (= (time/timestamp-string->date close-timestamp)
              (time/timestamp-string->date open-timestamp))
           ; Open & close is on the same day ...
           [:div {:style (styles/monitor-chart-y-labels-style)}
                 [:div (time/timestamp-string->time (:open-timestamp  (first kline-list)))]
                 [:div (time/timestamp-string->time (:close-timestamp (last  kline-list)))]]
           ; Open & close is on other days ...
           [:div {:style (styles/monitor-chart-y-labels-style)}
                 [:div (time/timestamp-string->date-time (:open-timestamp  (first kline-list)))]
                 [:div (time/timestamp-string->date-time (:close-timestamp (last  kline-list)))]])))

(defn- monitor-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [settings]}]
  [:div {:style (styles/monitor-details-style)}
        [elements/label {:content (get-in settings [:symbol :label])
                         :font-weight :extra-bold :font-size :xxl :layout :fit
                         :style {:opacity ".1"}}]
        [elements/label {:content (get-in settings [:interval :label])
                         :font-weight :extra-bold :font-size :xs :layout :fit
                         :style {:opacity ".1"}}]])

(defn- monitor-chart
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id {:keys [kline-list price-max price-min] :as monitor-props}]
  [:div {:style (styles/monitor-chart-style)
         :class "trader--monitor-chart"}
        [:svg {:preserve-aspect-ratio "none" :view-box "0 0 100 100"
               :style (styles/monitor-chart-svg-style)}
              [:polyline {:points (monitor-props->diagram-points   monitor-props)
                          :style  (styles/monitor-chart-line-style monitor-id monitor-props)}]]
        [monitor-chart-price-data monitor-id monitor-props]
        [monitor-details          monitor-id monitor-props]
        [price-box                monitor-id monitor-props]
        [price-sensors            monitor-id monitor-props]])




;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- interval-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id _]
  [elements/select {:initial-options engine/INTERVAL-OPTIONS
                    :get-label-f :label
                    :label "Interval"
                    :min-width :xxs
                    :on-select  [:trader/->monitor-settings-changed monitor-id]
                    :value-path [:trader :monitor monitor-id :settings :interval]}])

(defn- period-limit-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id _]
  [elements/select {:label "Period count"
                    :min-width :xxs
                    :initial-options [20 40 60 80 100 120 140 160 180 200]
                    :on-select  [:trader/->monitor-settings-changed monitor-id]
                    :value-path [:trader :monitor monitor-id :settings :limit]}])

(defn- symbol-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id _]
  [elements/select {:initial-options engine/SYMBOL-OPTIONS
                    :get-label-f :label
                    :label "Symbol"
                    :min-width :xxs
                    :on-select  [:trader/->monitor-settings-changed monitor-id]
                    :value-path [:trader :monitor monitor-id :settings :symbol]}])

(defn- monitor-settings-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id monitor-props]
  [:<> [:div {:style {:display :flex}}
             [interval-select monitor-id monitor-props]]
       [elements/horizontal-separator {}]
       [:div {:style {:display :flex}}
             [symbol-select monitor-id monitor-props]]
       [elements/horizontal-separator {}]
       [:div {:style {:display :flex}}
             [period-limit-select monitor-id monitor-props]]])

(defn- monitor-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id monitor-props]
  [:div {:style (styles/monitor-settings-style)}
        [monitor-settings-form monitor-id monitor-props]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- monitor-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id {:keys [settings-mode? watching?] :as monitor-props}]
  [:div {:style (styles/monitor-structure-style)
         :data-watching (boolean watching?)}
        [:div {:style (styles/monitor-body-style)}
              [react-transition/mount-animation {:animation-timeout 150 :mounted? (not settings-mode?)}
                                                [monitor-chart monitor-id monitor-props]]
              [react-transition/mount-animation {:animation-timeout 150 :mounted? settings-mode?}
                                                [monitor-settings monitor-id monitor-props]]
              [monitor-controls monitor-id monitor-props]]
        [monitor-chart-y-labels monitor-id monitor-props]])

(defn- monitor
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [monitor-id monitor-props]
  [components/stated monitor-id
                     {:base-props    monitor-props
                      :component   #'monitor-structure
                      :initializer [:trader/init-monitor!     monitor-id]
                      :subscriber  [:trader/get-monitor-props monitor-id]}])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  (let [monitor-ids (a/subscribe [:db/get-item [:trader :monitor :monitor-ids]])]
       (fn [] (reduce-indexed #(conj %1 ^{:key %3} [monitor %3 {:last-monitor?  (= %2 (dec (count @monitor-ids)))
                                                                :first-monitor? (= %2 0)}])
                               [:<> [:style {:type "text/css"} (styles/monitors-style-rules)]]
                               @monitor-ids))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/toggle-monitor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ monitor-id]]
      (if (r monitor-watching? db monitor-id)
          {:db (r pause-monitor! db monitor-id)}
          {:db (as-> db % (r start-monitor!      % monitor-id)
                          (r show-monitor-chart! % monitor-id))
           :dispatch [:trader/request-kline-data! monitor-id]})))

(a/reg-event-fx
  :trader/update-time!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ monitor-id]]
      (cond ; If monitor is watching and time is elapsed ...
            (and (r monitor-watching? db monitor-id)
                 (r time-elapsed?     db monitor-id))
            [:trader/request-kline-data! monitor-id]
            ; If monitor is watching and time is NOT elapsed ...
            (r monitor-watching? db monitor-id)
            {:db (r update-time! db monitor-id)
             :dispatch-later [{:ms 1000 :dispatch [:trader/update-time! monitor-id]}]})))

(a/reg-event-fx
  :trader/receive-kline-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ monitor-id response]]
      (let [kline-data (:trader/get-kline-data response)]
           {:db (as-> db % (r store-received-kline-data! % monitor-id kline-data)
                           (r init-timer!                % monitor-id))
            :dispatch-later [{:ms 1000 :dispatch [:trader/update-time! monitor-id]}]
            :dispatch       [:trader/log! :trader/monitor (get kline-data :uri)
                                                          (get kline-data :timestamp)]})))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/request-kline-data!
  (fn [{:keys [db]} [_ monitor-id]]
      [:sync/send-query! :trader/synchronize!
                         {:on-success [:trader/receive-kline-data! monitor-id]
                          :on-failure [:trader/->monitor-network-error monitor-id]
                          :on-sent    [:trader/log! :trader/monitor "Fetching data from server ..."]
                          :query      [`(:trader/get-kline-data ~(r get-monitor-settings db monitor-id))]}]))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/->monitor-network-error
  (fn [{:keys [db]} [_ monitor-id]]
      {:db (dissoc-in db [:trader :monitor :watching?])
       :dispatch [:trader/log! :trader/monitor (str "Network error: " monitor-id)]}))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/->monitor-settings-changed
  (fn [{:keys [db]} [_ monitor-id]]
      (if (r monitor-watching? db monitor-id)
          [:trader/toggle-monitor! monitor-id])))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:db/set-item! [:trader :monitor :monitor-ids] [(generate-monitor-id)]]})
