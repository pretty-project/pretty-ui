
(ns app-extensions.trader.monitor
    (:require [app-extensions.trader.engine :as engine]
              [app-extensions.trader.styles :as styles]
              [app-extensions.trader.sync   :as sync]
              [app-fruits.react-transition  :as react-transition]
              [mid-fruits.candy             :refer [param return]]
              [mid-fruits.css               :as css]
              [mid-fruits.format            :as format]
              [mid-fruits.keyword           :as keyword]
              [mid-fruits.loop              :refer [reduce-indexed]]
              [mid-fruits.map               :as map :refer [dissoc-in]]
              [mid-fruits.math              :as math]
              [mid-fruits.random            :as random]
              [mid-fruits.time              :as time]
              [mid-fruits.vector            :as vector]
              [x.app-components.api         :as components]
              [x.app-core.api               :as a :refer [r]]
              [x.app-elements.api           :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def DEFAULT-INTERVAL {:label "3 minutes" :value "3"})

; @constant (integer)
(def DEFAULT-LIMIT 200)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- diagram-close-points
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [kline-list total-high total-low]}]
  ; - Egy n sávos pont-diagram n+1 pont által írható le.
  ; - Az egyes periódusok záró pontja az adott periódus záró ára,
  ;   a nyító pontja az adott periódus nyitó ára (az előző periódus záró ára)
  ; - Az első periódus esetén nem elérhető adat az előző periódus záró ára,
  ;   ezért az első periódus nyitó pontja a periódus nyitó ára (y-open)
  (let [range (- total-high total-low)
        count (count kline-list)]
       (letfn [(f [o dex {:keys [close open]}]
                  (let [x1 nil
                        y1 (- 100 (math/percent range (- open  total-low)))
                        x2        (math/percent count (inc dex))
                        y2 (- 100 (math/percent range (- close total-low)))]
                       (if o (str o " "         x2 "," y2)
                             (str o "0," y1 " " x2 "," y2))))]
              (reduce-kv f nil kline-list))))

(defn- diagram-bars
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [kline-list total-high total-low]}]
  (let [range (- total-high total-low)
        count (count kline-list)]
       (letfn [(f [o dex {:keys [close open low high]}]
                  (let [height (css/percent (math/percent range (math/absolute (- close open))))
                        width  (css/percent (math/percent count 1))
                        bottom (css/percent (math/percent range (- (min open close) total-low)))
                        left   (css/percent (math/percent count dex))
                        color (if (< 0 (- close open)) "rgba(0,255,170,.2)" "rgba(255,0,170,.2)")]
                       (conj o [:div {:style {:position "absolute" :bottom bottom :left left :width width :height height
                                              :background-color color}}
                                     (let [range (math/absolute (- close open))
                                           deep (min open close)
                                           bottom (- deep low)
                                           bottom (css/percent (- 0 (math/percent range (- deep low))))
                                           height (css/percent (math/percent range (math/absolute (- high low))))]
                                          [:div {:style {:position "absolute" :bottom bottom :left "calc(50% - 1px)"
                                                         :height height :width "2px" :background-color color}}])])))]
              (reduce-kv f [:<>] kline-list))))

(defn- diagram-period-points
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [kline-list total-high total-low]}]
  (let [range (- total-high total-low)
        count (count kline-list)]
       (letfn [(f [o dex {:keys [close open]}]
                  (let [x1        (math/percent count dex)
                        x2        (math/percent count (inc dex))
                        y1 (- 100 (math/percent range (- open  total-low)))
                        y2 (- 100 (math/percent range (- close total-low)))]
                      (str o " " x1 "," y1 " " x1 "," y2 " " x2 "," y2)))]
              (reduce-kv f nil kline-list))))

(defn more-than-24h?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [kline-list]}]
  (let [close-timestamp (:close-timestamp (last  kline-list))
        open-timestamp  (:open-timestamp  (first kline-list))]
       (= (time/timestamp-string->date close-timestamp)
          (time/timestamp-string->date open-timestamp))))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-monitor-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:interval (get-in db [:trader :monitor :settings :interval :value])
   :limit    (get-in db [:trader :monitor :settings :limit])})

(defn- get-monitor-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [monitor-data  (r sync/get-response db :trader/download-monitor-data)
        monitor-props (get-in db [:trader :monitor])]
       (merge (dissoc monitor-data :kline-data)
              (get    monitor-data :kline-data)
              (param  monitor-props)
              {:subscribed? (r sync/subscribed? db :trader/monitor)
               :responsed?  (r sync/responsed?  db :trader/download-monitor-data)})))

(a/reg-sub :trader/get-monitor-props get-monitor-props)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- init-monitor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (as-> db % (assoc-in % [:trader :monitor :settings-mode?] true)
             (assoc-in % [:trader :monitor :settings]
                         {:limit    DEFAULT-LIMIT
                          :interval DEFAULT-INTERVAL})))

(defn- show-monitor-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (assoc-in db [:trader :monitor :settings-mode?] true))

(a/reg-event-db :trader/show-monitor-settings! show-monitor-settings!)

(defn- show-monitor-chart!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (assoc-in db [:trader :monitor :settings-mode?] false))

(a/reg-event-db :trader/show-monitor-chart! show-monitor-chart!)

(defn- highlight-kline-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ dex]]
  (assoc-in db [:trader :monitor :highlighted-kline] dex))

(a/reg-event-db :trader/highlight-kline-item! highlight-kline-item!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- connection-toggle-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id {:keys [subscribed?] :as module-props}]
  [:div {:style (styles/monitor-connection-toggle-button-style module-id module-props)}
        [elements/button {:icon     (if subscribed? :pause :play_arrow)
                          :on-click [:trader/toggle-monitor-connection!]
                          :preset   :default-icon-button}]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- monitor-tl-controls
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [:div {:style (styles/box-tl-controls-style)}
        [connection-toggle-button module-id module-props]])

(defn- monitor-bl-controls
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id {:keys [settings-mode?] :as module-props}]
  [:div {:style (styles/box-bl-controls-style)}
        (if settings-mode? [elements/button {:icon :show_chart
                                             :preset :default-icon-button
                                             :on-click [:trader/show-monitor-chart!]}]
                           [elements/button {:icon :settings
                                             :preset :default-icon-button
                                             :on-click [:trader/show-monitor-settings!]}])])

(defn- monitor-controls
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [:<> [monitor-tl-controls module-id module-props]
       [monitor-bl-controls module-id module-props]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-box-open-close-price
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [highlighted-kline kline-list]}]
  (let [{:keys [open close]} (vector/nth-item kline-list highlighted-kline)]
       [:<> [:div {:style (styles/price-box-labels-style)}
                  [:div "Open: "]
                  [:div "Close: "]]
            [:div {:style (styles/price-box-prices-style)}
                  [:div (format/decimals open  2) " USD"]
                  [:div (format/decimals close 2) " USD"]]]))

(defn- price-box-open-close-timestamp
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [highlighted-kline kline-list] :as module-props}]
  (let [{:keys [open-timestamp close-timestamp]} (vector/nth-item kline-list highlighted-kline)]
       [:<> (if (more-than-24h? module-props)
                ; Open & close is on the same day ...
                [:div {:style (styles/price-box-timestamps-style)}
                      [:div (time/timestamp-string->time open-timestamp)]
                      [:div (time/timestamp-string->time close-timestamp)]]
                [:div {:style (styles/price-box-timestamps-style)}
                      [:div (time/timestamp-string->date-time open-timestamp)]
                 [:div (time/timestamp-string->date-time close-timestamp)]])]))

(defn- price-box-low-high-price
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [highlighted-kline kline-list]}]
  (let [{:keys [low high]} (vector/nth-item kline-list highlighted-kline)]
       [:<> [:div {:style (styles/price-box-labels-style)}
                  [:div "Min: "]
                  [:div "Max: "]]
            [:div {:style (styles/price-box-low-high-style)}
                  [:div (format/decimals low  2) " USD"]
                  [:div (format/decimals high 2) " USD"]]]))

(defn- price-box-range
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [highlighted-kline kline-list]}]
  (let [{:keys [open close]} (vector/nth-item kline-list highlighted-kline)]
       [:div {:style (styles/price-box-range-style)}
             (let [diff (- close open)]
                  (if (< 0 diff) [:span {:style (styles/price-box-pos-diff-style)}
                                        (format/decimals (str "+" (- close open)) 2)]
                                 [:span {:style (styles/price-box-neg-diff-style)}
                                        (format/decimals (- close open) 2)]))]))

(defn- price-box-volume
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [highlighted-kline kline-list]}]
  (let [{:keys [volume]} (vector/nth-item kline-list highlighted-kline)]
       [:div {:style (styles/price-box-volume-style)}
             (format/round volume)]))

(defn- price-box
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id {:keys [highlighted-kline kline-list] :as module-props}]
  (if (some? highlighted-kline)
      (let [{:keys [open-timestamp close-timestamp open close high low]} (vector/nth-item kline-list highlighted-kline)]
           [:div {:class "trader--price-box" :style (styles/price-box-style)}
                 [price-box-open-close-price     module-id module-props]
                 [price-box-open-close-timestamp module-id module-props]
                 [price-box-low-high-price       module-id module-props]
                 [:div {:style (styles/column-style)}
                       [price-box-range  module-id module-props]
                       [price-box-volume module-id module-props]]])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-sensor
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props dex]
  [:div {:style (styles/price-sensor-style module-id module-props dex)
         :class "trader--sensor" :on-mouse-over #(a/dispatch [:trader/highlight-kline-item! dex])}])

(defn- price-sensors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id {:keys [kline-list] :as module-props}]
  (letfn [(f [o dex _] (conj o [price-sensor module-id module-props dex]))]
         (reduce-kv f [:div {:style (styles/price-sensors-style)}]
                      (param kline-list))))

(defn- monitor-chart-price-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [total-high total-low]}]
  [:div {:style (styles/monitor-chart-price-data-style)}
        [:span {:style (styles/monitor-chart-price-label-style)}
               (str "average:")]
        [:span {:style (styles/monitor-chart-price-value-style)}
               (format/decimals (/ (+ total-high total-low) 2) 2)]
        [:span {:style (styles/monitor-chart-price-label-style)}
               (str "min:")]
        [:span {:style (styles/monitor-chart-price-value-style)}
               (format/decimals (+ 0 total-high) 2)]
        [:span {:style (styles/monitor-chart-price-label-style)}
               (str "max:")]
        [:span {:style (styles/monitor-chart-price-value-style)}
               (format/decimals (+ 0 total-low) 2)]])

(defn- monitor-chart-x-labels
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [total-high total-low]}]
  [:div {:style (styles/monitor-chart-x-labels-style)}
        [elements/label {:content total-high :indent :right :style {:opacity ".2" :margin-right "24px"}
                         :font-weight :extra-bold :layout :fit :font-size :m}]
        [elements/label {:content total-low  :indent :right :style {:opacity ".2" :margin-right "24px"}
                         :font-weight :extra-bold :layout :fit :font-size :m}]])

(defn- monitor-chart-y-labels
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [kline-list] :as module-props}]
  (if (more-than-24h? module-props)
      ; Open & close is on the same day ...
      [:div {:style (styles/monitor-chart-y-labels-style)}
            [:div (time/timestamp-string->time (:open-timestamp  (first kline-list)))]
            [:div (time/timestamp-string->time (:close-timestamp (last  kline-list)))]]
      ; Open & close is on other days ...
      [:div {:style (styles/monitor-chart-y-labels-style)}
            [:div (time/timestamp-string->date-time (:open-timestamp  (first kline-list)))]
            [:div (time/timestamp-string->date-time (:close-timestamp (last  kline-list)))]]))

(defn- monitor-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [settings symbol]}]
  [:div {:style (styles/monitor-details-style)}
        [elements/label {:content symbol
                         :font-weight :extra-bold :font-size :xxl :layout :fit
                         :style {:opacity ".1"}}]
        [elements/label {:content (get-in settings [:interval :label])
                         :font-weight :extra-bold :font-size :xs :layout :fit
                         :style {:opacity ".1"}}]])

(defn- monitor-chart
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id {:keys [kline-list total-high total-low] :as module-props}]
  [:div {:style (styles/monitor-chart-style)
         :class "trader--monitor-chart"}
        [monitor-chart-x-labels module-id module-props]
        [monitor-details        module-id module-props]
        [:svg {:preserve-aspect-ratio "none" :view-box "0 0 100 100"
               :style (styles/monitor-chart-svg-style)}
              [:polyline {:points (diagram-close-points                      module-props)
                          :style  (styles/monitor-chart-line-style module-id module-props)}]]
        [:div {:style (styles/monitor-bars-overlay)}
              (diagram-bars module-props)]
        [monitor-chart-price-data module-id module-props]
        [price-box                module-id module-props]
        [price-sensors            module-id module-props]
        [sync/synchronizing-label module-id module-props]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- interval-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id _]
  [elements/select {:initial-options engine/INTERVAL-OPTIONS
                    :get-label-f :label
                    :label "Interval"
                    :min-width :xxs
                    :on-select  [:trader/monitor-settings-changed]
                    :value-path [:trader :monitor :settings :interval]}])

(defn- period-limit-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id _]
  [elements/select {:label "Period count"
                    :min-width :xxs
                    :initial-options [20 40 60 80 100 120 140 160 180 200]
                    :on-select  [:trader/monitor-settings-changed]
                    :value-path [:trader :monitor :settings :limit]}])

(defn- monitor-settings-form
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [:<> [:div {:style {:display :flex}}
             [interval-select module-id module-props]]
       [elements/horizontal-separator {}]
       [:div {:style {:display :flex}}
             [period-limit-select module-id module-props]]])

(defn- monitor-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [:div {:style (styles/monitor-settings-style)}
        [monitor-settings-form module-id module-props]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- monitor-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id {:keys [settings-mode? subscribed?] :as module-props}]
  [:div {:style (styles/box-structure-style) :data-subscribed (boolean subscribed?)}
        [:style {:type "text/css"} (styles/monitor-style-rules)]
        [:div {:style (styles/box-body-style)}
              [react-transition/mount-animation {:animation-timeout 150 :mounted? (not settings-mode?)}
                                                [monitor-chart module-id module-props]]
              [react-transition/mount-animation {:animation-timeout 150 :mounted? settings-mode?}
                                                [monitor-settings module-id module-props]]
              [monitor-controls module-id module-props]]
        [monitor-chart-y-labels module-id module-props]
        [elements/horizontal-separator {:size :xxl}]])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [components/stated :trader/monitor
                     {:render-f   #'monitor-structure
                      :subscriber [:trader/get-monitor-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/init-monitor!
  (fn [{:keys [db]} _]
      {:db (r init-monitor! db)
       :dispatch [:trader/toggle-monitor-connection!]}))

(a/reg-event-fx
  :trader/toggle-monitor-connection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if (r sync/subscribed? db :trader/monitor)
          [:trader/unsubscribe-from-query! :trader/monitor]
          {:db (r show-monitor-chart! db)
           :dispatch [:trader/subscribe-to-query! :trader/monitor
                                                  {:query [`(:trader/download-monitor-data ~(r get-monitor-settings db))]}]})))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/monitor-settings-changed
  (fn [{:keys [db]} _]
      (if (r sync/subscribed? db :trader/monitor)
          [:trader/unsubscribe-from-query! :trader/monitor])))
