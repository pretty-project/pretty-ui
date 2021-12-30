
(ns app-extensions.trader.settings
    (:require [mid-fruits.map     :refer [dissoc-in]]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [app-extensions.trader.styles :as styles]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (maps in vector)
(def INTERVAL-OPTIONS [{:label "1 minute"   :value "1"}
                       {:label "3 minutes"  :value "3"}
                       {:label "5 minute"   :value "5"}
                       {:label "15 minute"  :value "15"}
                       {:label "30 minute"  :value "30"}
                       {:label "60 minute"  :value "60"}
                       {:label "120 minute" :value "120"}
                       {:label "240 minute" :value "240"}
                       {:label "360 minute" :value "360"}
                       {:label "720 minute" :value "720"}
                       {:label "1 day"      :value "D"}
                       {:label "1 week"     :value "W"}
                       {:label "1 month"    :value "M"}])

; @constant (maps in vector)
(def SYMBOL-OPTIONS [{:label "ETH / USD" :value "ETHUSD"}
                     {:label "BTC / USD" :value "BTCUSD"}])

; @constant (map)
(def DEFAULT-SYMBOL {:label "ETH / USD" :value "ETHUSD"})

; @constant (map)
(def DEFAULT-INTERVAL {:label "3 minutes" :value "3"})

; @constant (integer)
(def DEFAULT-LIMIT 120)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- interval-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/select ::interval-select
                   {:initial-options INTERVAL-OPTIONS
                    :get-label-f :label
                    :label "Interval"
                    :min-width :xxs
                    :on-select  [:trader/->settings-changed]
                    :value-path [:trader :settings :interval]}])

(defn- period-limit-counter
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/label {:content "Period limit" :layout :fit}]
       [elements/counter ::period-limit-counter
                         {:layout :fit
                          :min-value 1
                          :max-value 200
                          :on-change  [:trader/->settings-changed]
                          :value-path [:trader :settings :limit]}]])

(defn- symbol-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/select ::symbol-select
                   {:initial-options SYMBOL-OPTIONS
                    :get-label-f :label
                    :label "Symbol"
                    :min-width :xxs
                    :on-select  [:trader/->settings-changed]
                    :value-path [:trader :settings :symbol]}])

(defn- settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [settings-id]
  [:div {:style (styles/settings-style)}
        [:div {:style {:display :flex}}
              [interval-select settings-id]]
        [elements/horizontal-separator {}]
        [:div {:style {:display :flex}}
              [symbol-select settings-id]]
        [elements/horizontal-separator {}]
        [period-limit-counter settings-id]])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [view-id]
  [settings view-id])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/->settings-changed
  (fn [{:keys [db]} _]
      (if (get-in db [:trader :monitor :watching?])
          [:trader/toggle-monitor!])))

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:db/set-item! [:trader :settings] {:symbol   DEFAULT-SYMBOL
                                                    :limit    DEFAULT-LIMIT
                                                    :interval DEFAULT-INTERVAL}]})
